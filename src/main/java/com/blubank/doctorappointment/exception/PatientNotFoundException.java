package com.blubank.doctorappointment.exception;

public class PatientNotFoundException extends RuntimeException {

    public PatientNotFoundException() {
        super("Patient Not Found");
    }
}
