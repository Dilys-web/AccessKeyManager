package com.accesskeymanager.AccessKeyManager.service;

import com.accesskeymanager.AccessKeyManager.DTO.request.*;
import com.accesskeymanager.AccessKeyManager.DTO.response.ChangePasswordResponse;
import com.accesskeymanager.AccessKeyManager.DTO.response.SignInResponse;
import com.accesskeymanager.AccessKeyManager.DTO.response.SignUpResponse;
import com.accesskeymanager.AccessKeyManager.DTO.response.VerifyResponse;
import com.accesskeymanager.AccessKeyManager.Email.EmailService;
import com.accesskeymanager.AccessKeyManager.Exception.EmailFailedException;
import com.accesskeymanager.AccessKeyManager.Exception.UserNotVerifiedException;
import com.accesskeymanager.AccessKeyManager.config.JwtService;
import com.accesskeymanager.AccessKeyManager.model.AppUser;
import com.accesskeymanager.AccessKeyManager.model.School;
import com.accesskeymanager.AccessKeyManager.model.Token;
import com.accesskeymanager.AccessKeyManager.repository.SchoolRepository;
import com.accesskeymanager.AccessKeyManager.repository.TokenRepository;
import com.accesskeymanager.AccessKeyManager.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.accesskeymanager.AccessKeyManager.Enum.ResponseConstant.ERROR;
import static com.accesskeymanager.AccessKeyManager.Enum.ResponseConstant.SUCCESS;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final OTPService otpService;

    private final EmailService emailService;
    private final SchoolService schoolService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SchoolRepository schoolRepository;
    private final TokenRepository tokenRepository;


    public ResponseEntity<SignInResponse> authenticate(SignInRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Enter valid credentials", e);
        }
        AppUser user = userRepository.findByEmail(request.email()).orElseThrow(() -> new RuntimeException("Email not found "));
        if (!user.isVerified()) {
            throw new UserNotVerifiedException("Account not verified");
        }
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new SignInResponse(token, SUCCESS, "Login successful"));
    }

    public ResponseEntity<SignUpResponse> register(SignUpRequest signUpRequest, HttpServletRequest request) {
        if (userRepository.existsByEmail(signUpRequest.email())) {
            throw new EntityExistsException("User already exists");
        }

        School school = createSchool(signUpRequest);

        AppUser user = new AppUser();
        user.setEmail(signUpRequest.email());
        user.setPassword(passwordEncoder.encode(signUpRequest.password()));
        user.setSchool(school);
        user.setRole(signUpRequest.role());
        AppUser appUser = userRepository.save(user);

        int otp = otpService.generateOtp(String.valueOf(appUser.getId()));

        sendOtp(request, appUser, otp);

        return ResponseEntity.status(HttpStatus.CREATED).body(new SignUpResponse(SUCCESS, appUser.getId(), school.getId()));

    }

    private School createSchool(SignUpRequest signUpRequest) {
        Optional<School> schoolFromDB = schoolRepository.findByEmailDomain(signUpRequest.schoolEmail());
        School school;
        if (schoolFromDB.isEmpty()) {
            School newSchool = new School();
            newSchool.setEmailDomain(signUpRequest.schoolEmail());
            newSchool.setName(signUpRequest.schoolName());
            school = schoolRepository.save(newSchool);
        } else {
            school = schoolFromDB.get();
        }
        return school;
    }

    private void sendOtp(HttpServletRequest request, AppUser appUser, int otp) {
        String verifyUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        System.out.println(verifyUrl);
        CompletableFuture<Boolean> otpFuture = otpService.sendOtp(appUser.getEmail(), otp, verifyUrl);
        try {
            boolean isOtpSent = otpFuture.get();
            if (!isOtpSent) {
                throw new EmailFailedException("Email failed to send");
            }

        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new EmailFailedException(e.getMessage(), e);
        }
    }


    public ResponseEntity<VerifyResponse> verify(VerifyRequest request) {
        Optional<AppUser> optionalUser = userRepository.findByEmail(request.email());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body(new VerifyResponse(ERROR, "User not found"));
        }
        AppUser user = optionalUser.get();
        int otpFromCache = otpService.generateOtp(String.valueOf(user.getId()));
        if (otpFromCache != request.otp()) {
            return ResponseEntity.badRequest().body(new VerifyResponse(ERROR, "Wrong Otp"));

        }

        user.setVerified(true);
        userRepository.save(user);

        return ResponseEntity.ok(new VerifyResponse(SUCCESS, "OTP verification successful."));

    }

    public ResponseEntity<ChangePasswordResponse> changePassword(ChangePasswordRequest changePasswordRequest, HttpServletRequest request) {
        // Retrieve the user by email
        Optional<AppUser> optionalUser = userRepository.findByEmail(changePasswordRequest.email());

        if (optionalUser.isEmpty()) {
            // If user not found, return error response
            return ResponseEntity.badRequest().body(new ChangePasswordResponse("User not found"));
        }

        AppUser user = optionalUser.get();
        if (!passwordEncoder.matches(changePasswordRequest.oldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ChangePasswordResponse("Your password doesn't match what we have"));
        }

        // Update the user's password
        user.setPassword(changePasswordRequest.newPassword());


        userRepository.save(user);

        String msg = "Your password has been changed \n";
        emailService.sendEmail(changePasswordRequest.email(), msg, "Password Changed");
        return ResponseEntity.status(HttpStatus.CREATED).body(new ChangePasswordResponse(user.getPassword()));
    }

    public void resetPassword(String token, String newPassword) {
        Token resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (LocalDateTime.now().isAfter(resetToken.getExpiresAt())) {
            throw new RuntimeException("Token has expired");
        }

        AppUser user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Optionally, invalidate the token after use
        tokenRepository.delete(resetToken);
    }

    public void forgotPassword(String email) throws MessagingException {
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with the email: " + email));
        String token = UUID.randomUUID().toString();
        Token resetToken = Token.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(30)) //Token expires in 30minutes
                .user(user)
                .build();
        tokenRepository.save(resetToken);
        emailService.sendPasswordResetEmail(user.getEmail(), "http://localhost:8080/activate-account" + "?token=" + token);

    }


}