package com.accesskeymanager.AccessKeyManager.DTO.response;

import com.accesskeymanager.AccessKeyManager.Enum.ResponseConstant;

import java.util.UUID;

public record SignUpResponse(
        ResponseConstant status,
        UUID userId,

        UUID schoolId


) {
}
