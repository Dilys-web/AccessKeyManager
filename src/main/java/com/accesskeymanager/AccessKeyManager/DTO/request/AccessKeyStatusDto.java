package com.accesskeymanager.AccessKeyManager.DTO.request;

import java.time.LocalDate;

public record AccessKeyStatusDto(
        String accessKeyId,
        String status,
        LocalDate expiryDate
) {

}
