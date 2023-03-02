package io.kubeomatic.timebombadmission.service;

public class TimerNotValidException extends Exception {
    public TimerNotValidException(String message) {
        super(message);
    }
}
