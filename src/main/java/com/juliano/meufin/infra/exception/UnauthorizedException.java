package com.juliano.meufin.infra.exception;


public class UnauthorizedException extends Throwable {
    public UnauthorizedException(String message) {
        super(message);
    }
}
