package com.accesskeymanager.AccessKeyManager.Exception;

public class InvalidOtpException extends RuntimeException {

    public InvalidOtpException(String invalidOtp){

        super(invalidOtp);
    }

    public InvalidOtpException(String invalidOtp, Throwable cause) {
        super(invalidOtp, cause);
    }


    }

