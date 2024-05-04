package com.accesskeymanager.AccessKeyManager.DTO.response;

import com.accesskeymanager.AccessKeyManager.Enum.KeyStatus;

import java.time.LocalDate;

public record AccessKeyResponseDto(
        String accessKey,
Long id,
        KeyStatus status,
        LocalDate expiryDate,
        LocalDate procurementDate

) {
}
