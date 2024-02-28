package com.blubank.doctorappointment.exception;

public class AppointmentNotFoundException extends RuntimeException {

    public AppointmentNotFoundException() {
        super("Open appointment not found");
    }
}
