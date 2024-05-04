package com.accesskeymanager.AccessKeyManager.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.naming.AuthenticationException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({Exception.class, EmailFailedException.class})
    public final ResponseEntity<ErrorResponse> exceptionResponse (Exception exception, WebRequest webRequest){
      ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), webRequest.getDescription(false));
      return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(errorResponse);
    }

    @ExceptionHandler({UserNotVerifiedException.class, InvalidOtpException.class})
    public final ResponseEntity<ErrorResponse> handleUserExceptions (Exception exception , WebRequest webRequest){
         ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), webRequest.getDescription(false));
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({AuthenticationException.class, InsufficientAuthenticationException.class, BadCredentialsException.class})
    public final ResponseEntity<ErrorResponse>  handleAuthenticationExceptions(Exception exception, WebRequest webRequest){
        exception.printStackTrace();
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), webRequest.getDescription(false));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

}
