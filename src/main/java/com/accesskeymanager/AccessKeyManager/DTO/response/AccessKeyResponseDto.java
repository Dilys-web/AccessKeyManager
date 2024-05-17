package com.accesskeymanager.AccessKeyManager.DTO.response;

import com.accesskeymanager.AccessKeyManager.Enum.KeyStatus;

import java.time.LocalDate;
import java.util.UUID;

public record AccessKeyResponseDto(
        String accessKey,
        UUID id,
        KeyStatus status,
        LocalDate expiryDate,
        LocalDate procurementDate

) {
}
