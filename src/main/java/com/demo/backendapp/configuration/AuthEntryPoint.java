package com.demo.backendapp.configuration;

import com.demo.backendapp.dto.response.ApiResponse;
import com.demo.backendapp.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class AuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErrorCode code = ErrorCode.UNAUTHENTICATED;

        response.setStatus(code.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse r = ApiResponse.builder()
                .code(code.getCode())
                .message(code.getMessage())
                .build();

        response.getWriter().write(new ObjectMapper().writeValueAsString(r));
        response.flushBuffer();
    }
}
