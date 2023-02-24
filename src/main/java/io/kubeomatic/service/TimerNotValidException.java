package io.kubeomatic.service;

public class TimerNotValidException extends Exception {
    public TimerNotValidException(String message) {
        super(message);
    }
}
