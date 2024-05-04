package com.accesskeymanager.AccessKeyManager.DTO.response;

import com.accesskeymanager.AccessKeyManager.Enum.ResponseConstant;

public record SignUpResponse(
        ResponseConstant status,
        Long body
) {
}
