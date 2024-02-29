package com.blubank.doctorappointment.exception;

public class TakenAppointmentException extends RuntimeException {

    public TakenAppointmentException() {
        super("This Appointment is already taken by a patient");
    }
}
