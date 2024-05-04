package com.accesskeymanager.AccessKeyManager.DTO.response;

import com.accesskeymanager.AccessKeyManager.Enum.ResponseConstant;
import jakarta.validation.constraints.NotNull;

public record SignInResponse(

        @NotNull
        String accessToken,
        ResponseConstant status,
        String message
) {

}

