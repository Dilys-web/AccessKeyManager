package com.accesskeymanager.AccessKeyManager.DTO.request;

import com.accesskeymanager.AccessKeyManager.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record SignUpRequest(
        @NotNull
        @Email
        String email,

        @NotNull
        @Email
        String schoolEmail,
        @NotNull
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters ")
        String password,
        String schoolName,
        Role role
     //   boolean isAdmin

) {


}
