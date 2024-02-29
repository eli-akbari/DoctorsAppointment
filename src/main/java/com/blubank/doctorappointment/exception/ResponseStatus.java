package com.blubank.doctorappointment.exception;

import org.springframework.http.HttpStatus;

public enum ResponseStatus {

    SUCCESS(HttpStatus.OK.value(), "Success"),
    END_TIME_BEFORE_START_TIME(HttpStatus.BAD_REQUEST.value(),"Error"),
    DURATION_TOO_SHORT(HttpStatus.BAD_REQUEST.value(),"Error"),
    DOCTOR_NOT_FOUND(HttpStatus.NOT_FOUND.value(),"Error"),
    APPOINTMENT_NOT_FOUND(HttpStatus.NOT_FOUND.value(),"Error"),
    CONFLICT(HttpStatus.NOT_ACCEPTABLE.value(),"Error"),
    MISSING_PATIENT_INFO(HttpStatus.BAD_REQUEST.value(),"Error"),
    PATIENT_NOT_FOUND(HttpStatus.NOT_FOUND.value(),"Error"),
    APPOINTMENT_ALREADY_TAKEN(HttpStatus.NOT_ACCEPTABLE.value(),"Error"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Error"),
    ;

    private final int code;
    private final String message;

    ResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
