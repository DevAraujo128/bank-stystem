package org.example.bank.system.exceptions;

public class ClienteDuplicadoException extends RuntimeException {
    public ClienteDuplicadoException(String message) {
        super(message);
    }
}
