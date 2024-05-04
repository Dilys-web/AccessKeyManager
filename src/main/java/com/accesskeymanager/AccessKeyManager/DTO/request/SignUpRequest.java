package com.accesskeymanager.AccessKeyManager.DTO.request;

import com.accesskeymanager.AccessKeyManager.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;


public record SignUpRequest(
        @NotNull
        @Email
        String email,

        @NotNull
        @Email
        String schoolEmail,
        @NotNull
        String password,
        String schoolName,
        Role role) {


}
