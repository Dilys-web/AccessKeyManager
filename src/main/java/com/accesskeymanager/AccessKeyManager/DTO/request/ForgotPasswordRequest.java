package com.accesskeymanager.AccessKeyManager.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record ForgotPasswordRequest(
        @Email(message = "Email not well formatted")
        @NotNull(message = "field required")
         String email

)   {
}


