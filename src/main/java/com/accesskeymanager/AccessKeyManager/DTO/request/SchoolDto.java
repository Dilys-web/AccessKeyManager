package com.accesskeymanager.AccessKeyManager.DTO.request;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;


public record SchoolDto (

         @NotBlank(message ="Name is required")
         String name,
         @NotBlank(message = "Email is required")
         String emailDomain,

         UUID userId
){
}
