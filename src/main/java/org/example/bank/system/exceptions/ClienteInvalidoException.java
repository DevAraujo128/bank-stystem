package org.example.bank.system.exceptions;

public class ClienteInvalidoException extends RuntimeException {
    public ClienteInvalidoException(String message) {
        super(message);
    }
}
