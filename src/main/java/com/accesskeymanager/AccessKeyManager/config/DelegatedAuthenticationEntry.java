package com.accesskeymanager.AccessKeyManager.config;

import com.accesskeymanager.AccessKeyManager.Exception.EmailFailedException;
import com.accesskeymanager.AccessKeyManager.Exception.InvalidOtpException;
import com.accesskeymanager.AccessKeyManager.Exception.UserNotVerifiedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Component("delegatedAuthenticationEntry")
public class DelegatedAuthenticationEntry  implements AuthenticationEntryPoint {
    private final HandlerExceptionResolver exceptionResolver;

    public DelegatedAuthenticationEntry(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
        this.exceptionResolver = exceptionResolver;
    }


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Object error = request.getAttribute("error");
        System.out.println(error);
        System.out.println(authException);
        switch (error){
            case InsufficientAuthenticationException insufficientAuthenticationException ->
                    exceptionResolver.resolveException(request, response, null, new InsufficientAuthenticationException(insufficientAuthenticationException.getMessage(),insufficientAuthenticationException.getCause()));
            case AccessDeniedException accessDeniedException -> exceptionResolver.resolveException(request, response, null, new AccessDeniedException(accessDeniedException.getMessage()));
            case EmailFailedException emailFailedException -> exceptionResolver.resolveException(request, response, null, new EmailFailedException(emailFailedException.getMessage(), emailFailedException.getCause()));
            case UserNotVerifiedException userNotVerifiedException -> exceptionResolver.resolveException(request, response, null, new UserNotVerifiedException(userNotVerifiedException.getMessage(), userNotVerifiedException.getCause()));
            case InvalidOtpException invalidOtpException -> exceptionResolver.resolveException(request, response, null, new InvalidOtpException(invalidOtpException.getMessage(),invalidOtpException.getCause()));
            case BadCredentialsException badCredentialsException -> exceptionResolver.resolveException(request, response, null, new BadCredentialsException("Enter valid credentials", badCredentialsException.getCause()));
            default -> exceptionResolver.resolveException(request, response, null, authException) ;

        }
    }
}
