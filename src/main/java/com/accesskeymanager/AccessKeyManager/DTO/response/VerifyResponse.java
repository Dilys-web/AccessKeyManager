package com.accesskeymanager.AccessKeyManager.DTO.response;

import com.accesskeymanager.AccessKeyManager.Enum.ResponseConstant;

public record VerifyResponse(
        ResponseConstant status,
        String message

) {


}
