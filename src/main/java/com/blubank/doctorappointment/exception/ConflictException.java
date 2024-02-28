package com.blubank.doctorappointment.exception;

public class ConflictException extends RuntimeException {

    public ConflictException() {
        super("Concurrency issue: Appointment is being taken by a patient simultaneously");
    }
}
