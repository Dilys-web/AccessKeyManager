package com.accesskeymanager.AccessKeyManager.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VerifyRequest(
        @NotNull(message = "field required")
        @Email(message = "Email required")
        String email,
        String otp
) {

}