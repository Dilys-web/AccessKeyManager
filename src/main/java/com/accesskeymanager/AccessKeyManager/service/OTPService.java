package com.accesskeymanager.AccessKeyManager.service;

import com.accesskeymanager.AccessKeyManager.Email.EmailService;
import com.accesskeymanager.AccessKeyManager.Exception.EmailFailedException;
import com.accesskeymanager.AccessKeyManager.model.AppUser;
import com.accesskeymanager.AccessKeyManager.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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

    //@Cacheable(value = "OtpCache", key="#userid")
    public int generateOtp(){
        return 100000 + random.nextInt(900000);


    }
    public boolean verifyOtp(int otpFromCache, int otp){
        return otpFromCache == otp;

    }

    @Async
    public CompletableFuture<Boolean> sendOtp(String email, int otp){
        CompletableFuture<Void> emailFuture = null;
        try {
            emailFuture = emailService.sendEmail(email, String.valueOf(otp), "Confirmation token");
        } catch (Exception e) {
            throw new EmailFailedException(e.getMessage(),e);
        }
        return emailFuture.thenApplyAsync(result-> true)
                .exceptionally(ex -> {
                    log.error("Otp not sent", ex);
                    return false;
                });

    }
    public Optional<AppUser> findByOtp(int otp ) {
        return userRepository.findByOtp(otp);
    }

}
