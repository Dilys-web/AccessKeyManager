package com.accesskeymanager.AccessKeyManager.DTO.request;

import com.accesskeymanager.AccessKeyManager.Enum.KeyStatus;

import java.time.LocalDate;

public record AccessKeySchoolDto(
        Long id,
        String accessKey,
        KeyStatus status,
        LocalDate dateOfProcurement,
        LocalDate expiryDate,

        String schoolName
) {

}
