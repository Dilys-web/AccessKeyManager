package com.accesskeymanager.AccessKeyManager.DTO.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.Random;

public record RecoverAccountResponse(
        @Email(message = "Email not well formatted")
        @NotNull(message = "field required")
        String email,

        String otp
) {

    private static String generateOTP() {
        Random random = new Random();
        int otpValue = 100000 + random.nextInt(900000);
        return String.valueOf(otpValue);
    }
}
