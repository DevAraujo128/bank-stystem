package org.example.bank.system.exceptions;

public class ContaDuplicadaException extends RuntimeException {
    public ContaDuplicadaException(String message) {
        super(message);
    }
}
