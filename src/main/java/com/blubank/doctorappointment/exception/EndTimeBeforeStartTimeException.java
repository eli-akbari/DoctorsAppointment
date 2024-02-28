package com.blubank.doctorappointment.exception;

public class EndTimeBeforeStartTimeException extends RuntimeException {

    public EndTimeBeforeStartTimeException() {
        super("End time cannot be before start time");
    }
}
