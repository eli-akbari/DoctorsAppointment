package com.blubank.doctorappointment.exception;

public class TakenAppointmentException extends RuntimeException {

    public TakenAppointmentException() {
        super("Cannot delete appointment, It is already taken by a patient");
    }
}
