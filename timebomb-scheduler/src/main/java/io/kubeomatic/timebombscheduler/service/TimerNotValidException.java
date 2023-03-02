package io.kubeomatic.timebombscheduler.service;

public class TimerNotValidException extends Exception {
    public TimerNotValidException(String message) {
        super(message);
    }
}
