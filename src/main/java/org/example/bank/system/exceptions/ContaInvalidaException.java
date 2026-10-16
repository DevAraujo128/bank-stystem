package org.example.bank.system.exceptions;

public class ContaInvalidaException extends RuntimeException {
    public ContaInvalidaException(String message) {
        super(message);
    }
}
