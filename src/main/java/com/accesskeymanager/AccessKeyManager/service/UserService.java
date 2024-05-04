package com.accesskeymanager.AccessKeyManager.service;

import com.accesskeymanager.AccessKeyManager.DTO.request.AccessKeyDto;
import com.accesskeymanager.AccessKeyManager.DTO.request.ResetPasswordRequest;
import com.accesskeymanager.AccessKeyManager.DTO.request.SignInRequest;
import com.accesskeymanager.AccessKeyManager.DTO.request.SignUpRequest;
import com.accesskeymanager.AccessKeyManager.DTO.response.ResetPasswordResponse;
import com.accesskeymanager.AccessKeyManager.DTO.response.SignInResponse;
import com.accesskeymanager.AccessKeyManager.DTO.response.SignUpResponse;
import com.accesskeymanager.AccessKeyManager.DTO.response.VerifyResponse;
import com.accesskeymanager.AccessKeyManager.Email.EmailService;
import com.accesskeymanager.AccessKeyManager.Enum.ResponseConstant;
import com.accesskeymanager.AccessKeyManager.Exception.EmailFailedException;
import com.accesskeymanager.AccessKeyManager.Exception.UserNotVerifiedException;
import com.accesskeymanager.AccessKeyManager.config.JwtService;
import com.accesskeymanager.AccessKeyManager.model.AppUser;
import com.accesskeymanager.AccessKeyManager.model.School;
import com.accesskeymanager.AccessKeyManager.repository.SchoolRepository;
import com.accesskeymanager.AccessKeyManager.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
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

    private  final EmailService emailService;
    private final AccessKeyService accessKeyService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SchoolRepository schoolRepository;

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
        Optional<School> school = schoolRepository.findByEmailDomain(signUpRequest.schoolEmail());
        AppUser user = null;
//        if (school.isEmpty())
//
////        AppUser user = AppUser.builder()
////                .email(signUpRequest.email())
////                .password(signUpRequest.password())
////                .school(school)
////                .role(signUpRequest.role())
////                .build();
//        {
//        }

        int otp = otpService.generateOtp();
        user = new AppUser();
        user.setEmail(signUpRequest.email());
        user.setPassword(passwordEncoder.encode(signUpRequest.password()));
        user.setSchool(school.orElse(null));
        user.setRole(signUpRequest.role());
        user.setOtp(otp);
        AppUser appUser = userRepository.save(user);


        CompletableFuture<Boolean> otpFuture = otpService.sendOtp(appUser.getEmail(), otp);
        try {
            boolean isOtpSent = otpFuture.get();
            if (!isOtpSent) {
                throw new EmailFailedException("Email failed to send");
            }

        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new EmailFailedException(e.getMessage(), e);
        }


        String verifyUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        System.out.println(verifyUrl);
        String msg = "Click on the link below to verify your email address \n";
        msg += verifyUrl + "/api/v1/auth/verify-email?otp=" + otp;
        emailService.sendEmail(signUpRequest.email(), msg, "Email Verification");
        return ResponseEntity.status(HttpStatus.CREATED).body(new SignUpResponse(SUCCESS, appUser.getId()));

    }

    // after user is verified access key should be generated

    public ResponseEntity<VerifyResponse>verify(int otp) {
        Optional<AppUser> optionalUser = userRepository.findByOtp(otp);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body(new VerifyResponse(ERROR, "User not found"));
        }
        AppUser user = optionalUser.get();
        user.setVerified(true);
        userRepository.save(user);

        // Generate Access Key
        AccessKeyDto AccessKeyDto = new AccessKeyDto(
                user.getId(),
                user.getEmail(),
                user.getSchool().getId(),
                user.getSchool().getName(),
                user.getSchool().getId(),
                user.getRole(),
                user.getExpiryDate(),
                user.getAccessKey()
        );

        AccessKeyService accessKeyService = new AccessKeyService();
        AccessKeyDto generatedAccessKey = accessKeyService.generateAccessKey(AccessKeyDto);

        return ResponseEntity.ok(new VerifyResponse(SUCCESS, "OTP verification successful. Access key generated: " + generatedAccessKey.getAccessKey()));
    }
//        accessKeyDto.setUserId(user.getId());
//        accessKeyDto.userId()
//      AppUser user1 =   userRepository.getById(user.getId());
//
//        AccessKeyDto accssKeyDto = new AccessKeyDto(user1.getId(),user1.getUser().getEmail(),user.getUser().getId(), access.getSchool().getName(), access.getSchool().getId(),
//                user.getStatus(), user.getDateOfProcurement(), access.getExpiryDate(), access.getAccessKey());
//
//
//        AccessKeyService accessKeyService =  new AccessKeyService();
//        accessKeyService.generateAccessKey(accssKeyDto);
//        if (request.otp() != otp) {
//            throw new InvalidOtpException("Invalid OTP");
//        }
    
    }

          public ResponseEntity<ResetPasswordResponse> resetPassword(ResetPasswordRequest resetPasswordRequest) {
         // Retrieve the user by email
            Optional<AppUser> optionalUser = userRepository.findByEmail(resetPasswordRequest.email());

            if (optionalUser.isEmpty()) {
                // If user not found, return error response
                return ResponseEntity.badRequest().body(new ResetPasswordResponse("User not found"));
            }

            AppUser user = optionalUser.get();

            // Update the user's password
            ((AppUser) user).setPassword(resetPasswordRequest.newPassword());
            userRepository.save(user);

             //Return success response
            return ResponseEntity.ok(new ResetPasswordResponse("Password reset successful"));
        }



//    public ResponseEntity<ResetPasswordResponse> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
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



//    create a verify method:
//    create response and request dtos for it
//    get the otp from the cache using the same code at line 68
//    compare it to the one in the request, if they are the same return ResponseEntity.ok()
//    If they are not the same throw an exception for it

//    public Optional<AppUser> findByEmail(String email) {
//
//        return userRepository.findByEmail(email);
//    }





