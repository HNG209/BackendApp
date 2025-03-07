package com.demo.backendapp.controller;

import com.demo.backendapp.dto.request.AuthenticationRequest;
import com.demo.backendapp.dto.request.TokenValidationRequest;
import com.demo.backendapp.dto.response.ApiResponse;
import com.demo.backendapp.dto.response.AuthenticationResponse;
import com.demo.backendapp.dto.response.TokenValidationResponse;
import com.demo.backendapp.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) throws JOSEException {
        AuthenticationResponse r = authenticationService.authenticate(request);

        return ApiResponse.<AuthenticationResponse>builder()
                .result(r)
                .build();
    }

    //not useful anymore
    @PostMapping("/validate")
    public ApiResponse<TokenValidationResponse> validateToken(@RequestBody TokenValidationRequest request) throws ParseException, JOSEException {
        TokenValidationResponse response = authenticationService.validateToken(request);

        return ApiResponse.<TokenValidationResponse>builder()
                .result(response)
                .build();
    }
}
