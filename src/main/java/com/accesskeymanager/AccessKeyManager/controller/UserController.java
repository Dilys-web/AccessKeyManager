package com.accesskeymanager.AccessKeyManager.controller;


import com.accesskeymanager.AccessKeyManager.DTO.request.ResetPasswordRequest;
import com.accesskeymanager.AccessKeyManager.DTO.request.SignInRequest;
import com.accesskeymanager.AccessKeyManager.DTO.request.SignUpRequest;
import com.accesskeymanager.AccessKeyManager.DTO.response.ResetPasswordResponse;
import com.accesskeymanager.AccessKeyManager.DTO.response.SignInResponse;
import com.accesskeymanager.AccessKeyManager.DTO.response.SignUpResponse;
import com.accesskeymanager.AccessKeyManager.DTO.response.VerifyResponse;
import com.accesskeymanager.AccessKeyManager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/")
@AllArgsConstructor
public class UserController {
    @Autowired
    private  UserService userService;

    @PostMapping("signup")
    @Operation(summary = "registering a user")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody @Valid SignUpRequest signUpRequest, final HttpServletRequest request) {
             return userService.register(signUpRequest,  request);
    }

    @PostMapping("login")
    @Operation(summary = "authenticating a user")
    public ResponseEntity<SignInResponse> login(@RequestBody SignInRequest signInRequest) {
        return userService.authenticate(signInRequest);
    }
    @GetMapping("verify-email")
    @Operation(summary = "user verification")
    public ResponseEntity<VerifyResponse>verify(@RequestParam("otp")  int otp){

        return userService.verify(otp);
    }

    @PostMapping("reset-password")
    public ResponseEntity<ResetPasswordResponse>resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest){
        return userService.resetPassword(resetPasswordRequest);
    }


}

