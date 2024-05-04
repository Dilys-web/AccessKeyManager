package com.accesskeymanager.AccessKeyManager.DTO.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;


public record SchoolDto (

         @NotBlank(message ="Name is required")
         String name,
          @NotBlank(message = "Email is required")
         String emailDomain,

         Long userId
){
}
