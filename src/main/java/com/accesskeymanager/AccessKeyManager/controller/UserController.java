package com.accesskeymanager.AccessKeyManager.controller;


import com.accesskeymanager.AccessKeyManager.DTO.request.*;
import com.accesskeymanager.AccessKeyManager.DTO.response.ChangePasswordResponse;
import com.accesskeymanager.AccessKeyManager.DTO.response.SignInResponse;
import com.accesskeymanager.AccessKeyManager.DTO.response.SignUpResponse;
import com.accesskeymanager.AccessKeyManager.DTO.response.VerifyResponse;
import com.accesskeymanager.AccessKeyManager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/auth/")
@AllArgsConstructor
public class UserController {
    private  final UserService userService;

    @PostMapping("signup")
    @Operation(summary = "registering a user")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody @Valid SignUpRequest signUpRequest, final HttpServletRequest request) {
             return userService.register(signUpRequest,  request);
    }

    @PostMapping("login")
    @Operation(summary = "authenticating a user")
    public ResponseEntity<SignInResponse> login(@RequestBody @Valid SignInRequest signInRequest) {
        return userService.authenticate(signInRequest);
    }
    @GetMapping("verify-email")
    @Operation(summary = "user verification")
    public ResponseEntity<VerifyResponse>verify(@RequestParam(value = "otp", required = false)  String otp, @RequestParam(value = "email", required = false)  String email, @RequestBody(required = false) VerifyRequest request){
        return userService.verify(Objects.requireNonNullElseGet(request, () -> new VerifyRequest(email, otp)));
    }

    @PostMapping("change-password")
    @Operation(summary = "change password")
    public ResponseEntity<ChangePasswordResponse>changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest, HttpServletRequest request){
        return userService.changePassword(changePasswordRequest, request);
    }
    @PostMapping("reset-password")
    @Operation(summary = "reset password")
    @ResponseStatus(HttpStatus.OK)
    public void resetPassword(@RequestParam String token, @RequestBody @Valid String newPassword) {
        userService.resetPassword(token, newPassword);
    }
    @PostMapping("forgot-password")
    @Operation(summary = "forgot password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
        userService.forgotPassword(forgotPasswordRequest.email());
        return ResponseEntity.ok().body("Forgot password email sent.");
    }


}

