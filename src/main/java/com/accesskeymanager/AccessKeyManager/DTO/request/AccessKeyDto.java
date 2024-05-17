package com.accesskeymanager.AccessKeyManager.DTO.request;

import com.accesskeymanager.AccessKeyManager.Enum.KeyStatus;

import java.time.LocalDate;
import java.util.UUID;

public record AccessKeyDto(
        UUID id,
        String accessKey,
        KeyStatus status,
        LocalDate dateOfProcurement,
        LocalDate expiryDate


) {

}
