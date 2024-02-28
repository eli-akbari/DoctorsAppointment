package com.blubank.doctorappointment.exception;

public class MissingPatientInfoException extends RuntimeException {

    public MissingPatientInfoException() {
        super("Patient name and phone number are required");
    }
}
