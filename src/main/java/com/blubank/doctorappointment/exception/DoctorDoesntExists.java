package com.blubank.doctorappointment.exception;

public class DoctorDoesntExists extends RuntimeException {

    public DoctorDoesntExists() {
        super("Doctor Not Found");
    }
}
