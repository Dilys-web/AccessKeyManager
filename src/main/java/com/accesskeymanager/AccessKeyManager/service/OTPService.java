package com.accesskeymanager.AccessKeyManager.service;

import com.accesskeymanager.AccessKeyManager.Email.EmailService;
import com.accesskeymanager.AccessKeyManager.Exception.EmailFailedException;
import com.accesskeymanager.AccessKeyManager.model.AppUser;
import com.accesskeymanager.AccessKeyManager.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
@Slf4j
public class OTPService {
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final Random random= new Random();
    private final CacheManager cacheManager;
    @Cacheable(value = "OtpCache", key="#userid")

    public int generateOtp(String userid){
        Objects.requireNonNull(cacheManager.getCache("OtpCache")).evictIfPresent(userid);
        return 100000 + random.nextInt(900000);


    }
    public boolean verifyOtp(int otpFromCache, int otp){
        return otpFromCache == otp;

    }

    @Async
    public CompletableFuture<Boolean> sendOtp(String email, int otp, String verificationLink){
        CompletableFuture<Void> emailFuture;
        String msg = "Click on the link below to verify your email address \n Or use the otp code provided \n";
        msg += "Link: " + verificationLink + "/api/v1/auth/verify-email?" + "email=" + email + "&" +"otp=";
        msg += otp + "\n";
        msg += "OTP: " + otp +"\n";
        msg += "You have 5 minutes before otp expires";
        try {
            emailFuture = emailService.sendEmail(email, msg, "Email Verification");
        } catch (Exception e) {
            throw new EmailFailedException(e.getMessage(),e);
        }
        return emailFuture.thenApplyAsync(result-> true)
                .exceptionally(ex -> {
                    log.error("Otp not sent", ex);
                    return false;
                });

    }

}
