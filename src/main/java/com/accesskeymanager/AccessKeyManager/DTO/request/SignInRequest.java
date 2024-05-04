package com.accesskeymanager.AccessKeyManager.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record SignInRequest(
        @NotBlank(message = "field required")
        @Email(message = "Email required")
        String email,
        @NotEmpty(message = "field required")
        @NotBlank(message = "field required")
        @Size(min = 8, message = "Password should be 8 characters long")
        String password
) {
}
