package com.works.foodapi.infrastructure.service.email;

public class EmailException extends RuntimeException{

    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailException(String cause) {
        super(cause);
    }
}
