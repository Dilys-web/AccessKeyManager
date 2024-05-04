package com.accesskeymanager.AccessKeyManager.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record VerifyRequest(
        @NotNull
        @Email
        String email,
        int otp
) {

}