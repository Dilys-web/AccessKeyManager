package com.accesskeymanager.AccessKeyManager.Email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String username;
    @Async
    public CompletableFuture<Void> sendEmail(String userEmail, String body, String subject){
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(userEmail);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);
        simpleMailMessage.setFrom(username);
        try{
            mailSender.send(simpleMailMessage);
            completableFuture.complete(null);
            log.info("Successfully sent mail");
        }catch (MailException mailException){
            log.error("Mail not sent",mailException);
        }
        return completableFuture;


    }

    @Async
    public CompletableFuture<Void> sendPasswordResetEmail(String userEmail, String resetUrl){
        String subject = "Password Reset Request";
        String body = "Hello " + userEmail + ",\n\n"
                + "You have requested to reset your password. Please click on the link below to reset your password:\n\n"
                + resetUrl + "\n\n"
                + "If you did not request this, please ignore this email.\n\n"
                + "Thank you,\nThe Micro-Focus Inc Team";
        return sendEmail(userEmail, body, subject);
    }

}
