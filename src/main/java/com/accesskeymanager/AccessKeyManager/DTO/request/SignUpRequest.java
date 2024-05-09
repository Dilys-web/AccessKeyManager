package com.accesskeymanager.AccessKeyManager.DTO.request;

import com.accesskeymanager.AccessKeyManager.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record SignUpRequest(
        @NotNull
        @Email
        String email,


        @NotBlank
        String password,
        String schoolName


) {


}
