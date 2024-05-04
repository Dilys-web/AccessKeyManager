package com.accesskeymanager.AccessKeyManager.Exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({Exception.class, EmailFailedException.class})
    public final ResponseEntity<ErrorResponse> exceptionResponse (Exception exception, WebRequest webRequest){
      ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), webRequest.getDescription(false));
      exception.printStackTrace();
      return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(errorResponse);
    }

    @ExceptionHandler({UserNotVerifiedException.class, InvalidOtpException.class})
    public final ResponseEntity<ErrorResponse> handleUserExceptions (Exception exception , WebRequest webRequest){
         ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), webRequest.getDescription(false));
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public final ResponseEntity<ErrorResponse> handlerNotFoundExceptions(Exception exception, WebRequest request){
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler({AuthenticationException.class, InsufficientAuthenticationException.class, AccessDeniedException.class, BadCredentialsException.class})
    public final ResponseEntity<ErrorResponse>  handleAuthenticationExceptions(Exception exception, WebRequest webRequest){
        System.out.println("dddddddd");
        exception.printStackTrace();
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), webRequest.getDescription(false));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
        Map<String, String> errors = new HashMap<>();
        exp.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
