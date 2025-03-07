package com.demo.backendapp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED(9999, "uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_EXISTED(1001, "username existed", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(2001, "user not found", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(2002, "role not found", HttpStatus.NOT_FOUND),
    PASSWORD_INVALID(3001, "invalid password", HttpStatus.BAD_REQUEST),
    KEY_INVALID(3002, "invalid enum key", HttpStatus.INTERNAL_SERVER_ERROR),
    BODY_NOT_SPECIFIED(3003, "body not specified", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(4004, "unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(4003, "access denied", HttpStatus.FORBIDDEN)
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
