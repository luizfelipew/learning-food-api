package com.works.foodapi.infrastructure.service.storage;

public class StorageException extends RuntimeException{

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageException(String cause) {
        super(cause);
    }
}
