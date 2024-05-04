package com.accesskeymanager.AccessKeyManager.Exception;

import java.io.Serial;

public class OperationFailedException extends RuntimeException{
    @Serial
    private static final long serialVersionUID= 2L;

    public OperationFailedException(String message){
        super(message);
    }
}
