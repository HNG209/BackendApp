package com.demo.backendapp.service;

import com.demo.backendapp.dto.request.AuthenticationRequest;
import com.demo.backendapp.dto.request.LogoutRequest;
import com.demo.backendapp.dto.request.TokenValidationRequest;
import com.demo.backendapp.dto.response.AuthenticationResponse;
import com.demo.backendapp.dto.response.TokenValidationResponse;
import com.demo.backendapp.entity.LogoutToken;
import com.demo.backendapp.entity.Permission;
import com.demo.backendapp.entity.User;
import com.demo.backendapp.exception.AppException;
import com.demo.backendapp.exception.ErrorCode;
import com.demo.backendapp.repository.LogoutTokenRepository;
import com.demo.backendapp.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LogoutTokenRepository logoutTokenRepository;

    protected static final String SECRET_KEY = "3a24de43eacbcd8c28709e63e36cd624b252d76b76773b45b150b023834f9d77";

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws JOSEException {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean authenticated = request.getPassword().matches(user.getPassword());//not encoded

        if(!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        String token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    public TokenValidationResponse validateToken(TokenValidationRequest request) throws JOSEException, ParseException {
        String token = request.getToken();
        JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date exp = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(verifier) && exp.after(new Date());

        return TokenValidationResponse.builder()
                .validated(verified)
                .username(verified ? signedJWT.getJWTClaimsSet().getSubject() : null)
                .build();
    }

    public SignedJWT verify(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date exp = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(verifier) && exp.after(new Date());

        if(!verified || logoutTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    public String generateToken(User user) throws JOSEException {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("HungIT")
                .issueTime(new Date())
                .claim("scope", buildScope(user))
                .jwtID(UUID.randomUUID().toString())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        jwsObject.sign(new MACSigner(SECRET_KEY.getBytes(StandardCharsets.UTF_8)));

        return jwsObject.serialize();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        SignedJWT token = verify(request.getToken());

        LogoutToken logoutToken = LogoutToken.builder()
                .id(token.getJWTClaimsSet().getJWTID())
                .exp(token.getJWTClaimsSet().getExpirationTime())
                .build();

        logoutTokenRepository.save(logoutToken);
    }

    private String buildScope(User user) {
        StringJoiner joiner = new StringJoiner(" ");
        if(!user.getRoles().isEmpty())
            user.getRoles().forEach(role -> {
                    joiner.add("ROLE_" + role.getName());

                    role.getPermissions()
                            .stream()
                            .map(Permission::getName)
                            .forEach(joiner::add);
                }
            );

        return joiner.toString();
    }
}
