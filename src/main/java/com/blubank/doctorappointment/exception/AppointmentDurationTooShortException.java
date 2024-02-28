package com.blubank.doctorappointment.exception;

public class AppointmentDurationTooShortException extends RuntimeException {

    public AppointmentDurationTooShortException() {
        super("Appointment duration should be at least 30 minutes");
    }
}
