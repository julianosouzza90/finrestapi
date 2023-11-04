package com.juliano.meufin.infra.exception;

public class ConflictException extends Throwable{
    public ConflictException(String message) {
        super(message);
    }
}
