package com.accesskeymanager.AccessKeyManager.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record SignUpRequest(
        @NotNull
        @Email
        String email,

 
        @NotBlank
        String password,
        String schoolName


) {


}
