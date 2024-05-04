package com.accesskeymanager.AccessKeyManager.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @Email(message = "Email not well formatted")
        @NotNull(message = "field required")
        String email,

        @NotBlank(message = "field required")
        @NotNull(message = "field required")
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
        String oldPassword,

        @NotBlank(message = "field required")
        @NotNull(message = "field required")
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
        String newPassword) {

}
