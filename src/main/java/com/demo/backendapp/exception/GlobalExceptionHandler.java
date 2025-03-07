package com.demo.backendapp.exception;

import com.demo.backendapp.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException exception){//app exceptions
        ApiResponse response = new ApiResponse();

        response.setCode(exception.getErrorCode().getCode());
        response.setMessage(exception.getMessage());

        return ResponseEntity
                .status(exception.getErrorCode().getStatusCode())
                .body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException exception){
        ApiResponse response = new ApiResponse();

        String key = exception.getFieldError().getDefaultMessage();
        ErrorCode code = ErrorCode.KEY_INVALID;

        try {
            code = ErrorCode.valueOf(key);
        } catch (IllegalArgumentException e){

        }

        response.setCode(code.getCode());
        response.setMessage(code.getMessage());

        return ResponseEntity
                .status(code.getStatusCode()).body(response);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    ResponseEntity<ApiResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception){
        ApiResponse response = new ApiResponse();
        ErrorCode code = ErrorCode.BODY_NOT_SPECIFIED;

        response.setCode(code.getCode());
        response.setMessage(code.getMessage());

        return ResponseEntity
                .status(code.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handleUncategorizedException(RuntimeException exception){//other exceptions
        ApiResponse response = new ApiResponse();
        ErrorCode code = ErrorCode.UNCATEGORIZED;

        response.setCode(code.getCode());
        response.setMessage(code.getMessage() + " - Type:" + exception);

        return ResponseEntity
                .status(code.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(value = AuthorizationDeniedException.class)
    ResponseEntity<ApiResponse> handleAccessDeniedException(AuthorizationDeniedException exception){
        ApiResponse response = new ApiResponse();
        ErrorCode code = ErrorCode.UNAUTHORIZED;

        response.setCode(code.getCode());
        response.setMessage(code.getMessage());

        return ResponseEntity
                .status(code.getStatusCode())
                .body(response);
    }
}
