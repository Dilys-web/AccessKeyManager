//package com.accesskeymanager.AccessKeyManager.controller;
//
//
//// UserController.java
//
//import com.accesskeymanager.AccessKeyManager.model.AppUser;
//import com.accesskeymanager.AccessKeyManager.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api")
//public class UserController {
//    @Autowired
//    private UserService userService;
//
//    @PostMapping("/signup")
//    public ResponseEntity<String> signUp(@RequestBody SignUpRequest signUpRequest) {
//        // Validate request
//        if (signUpRequest.getEmail() == null || signUpRequest.getPassword() == null) {
//            return ResponseEntity.badRequest().body("Email and password are required.");
//        }
//
//        // Check if user already exists
//        Optional<User> existingUser = userService.findByEmail(signUpRequest.getEmail());
//        if (existingUser.isPresent()) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with this email already exists.");
//        }
//
//        // Create new user
//        AppUser newUser = userService.register(signUpRequest.getEmail(), signUpRequest.getPassword(), Role.SCHOOL_IT_PERSONNEL, null);
//        return ResponseEntity.ok("User registered successfully.");
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
//        // Validate request
//        if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
//            return ResponseEntity.badRequest().body("Email and password are required.");
//        }
//
//        // Authenticate user
//        Optional<AppUser> user = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
//        if (user.isPresent()) {
//            return ResponseEntity.ok("Login successful.");
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
//        }
//    }
//
//    @PostMapping("/reset-password")
//    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
//        // Validate request
//        if (resetPasswordRequest.getEmail() == null) {
//            return ResponseEntity.badRequest().body("Email is required.");
//        }
//
//        // Check if user exists
//        Optional<AppUser> user = userService.findByEmail(resetPasswordRequest.getEmail());
//        if (user.isPresent()) {
//            // Reset password logic here
//            return ResponseEntity.ok("Password reset instructions sent to your email.");
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
//        }
//    }
//}
//
