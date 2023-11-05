package com.juliano.meufin.infra.exception;

public class CreateInvoiceException extends RuntimeException {
    public CreateInvoiceException(String message) {
        super(message);
    }
}
