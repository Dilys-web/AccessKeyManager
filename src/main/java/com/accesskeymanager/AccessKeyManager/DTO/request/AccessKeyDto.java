package com.accesskeymanager.AccessKeyManager.DTO.request;

import com.accesskeymanager.AccessKeyManager.Enum.KeyStatus;

import java.time.LocalDate;

public record AccessKeyDto(
        Long id,

 String user,
 Long userId,

 String school,
 Long schoolId,

 KeyStatus status,

 LocalDate dateOfProcurement,

 LocalDate expiryDate,

 String accessKey
) {

}
