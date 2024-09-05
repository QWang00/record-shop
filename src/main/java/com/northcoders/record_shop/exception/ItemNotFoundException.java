package com.northcoders.record_shop.exception;

public class ItemNotFoundException extends RuntimeException{
    public ItemNotFoundException() {
    }

    public ItemNotFoundException(String message) {
        super(message);
    }
}