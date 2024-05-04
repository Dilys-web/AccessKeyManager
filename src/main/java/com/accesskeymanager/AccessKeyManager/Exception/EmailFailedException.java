package com.accesskeymanager.AccessKeyManager.Exception;

import java.io.Serial;

public class EmailFailedException extends RuntimeException{

    @Serial
    private static final long serialVersionUID= 2L;

    public EmailFailedException(String message){
        super(message);
    }
    public EmailFailedException(String message, Throwable cause){

        super(message, cause);
    }

}
