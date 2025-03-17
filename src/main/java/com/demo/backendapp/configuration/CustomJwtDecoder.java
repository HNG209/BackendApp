package com.demo.backendapp.configuration;

import com.demo.backendapp.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Autowired
    private AuthenticationService authenticationService;

    protected static final String SECRET_KEY = "3a24de43eacbcd8c28709e63e36cd624b252d76b76773b45b150b023834f9d77";

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    public CustomJwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "HS512");
        nimbusJwtDecoder = NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            authenticationService.verify(token);
            System.out.println("verify");
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }

        return nimbusJwtDecoder.decode(token);
    }
}
