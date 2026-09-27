package org.example;

public class InsufficientBalanceException extends PlatformException {

    public InsufficientBalanceException(String message) {
        super(message);
    }
}