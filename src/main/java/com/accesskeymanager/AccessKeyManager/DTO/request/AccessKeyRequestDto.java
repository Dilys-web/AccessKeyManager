package com.accesskeymanager.AccessKeyManager.DTO.request;

import java.time.LocalDate;

public record AccessKeyRequestDto(

        String userEmail,

        Long schoolId

)
 {
}
