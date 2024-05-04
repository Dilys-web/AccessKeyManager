package com.accesskeymanager.AccessKeyManager.Exception;

public class AccessKeyNotFoundException extends RuntimeException {
    public AccessKeyNotFoundException(String message) {
        super(message);
    }
}