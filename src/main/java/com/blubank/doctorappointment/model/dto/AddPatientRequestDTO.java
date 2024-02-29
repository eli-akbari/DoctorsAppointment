package com.blubank.doctorappointment.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddPatientRequestDTO implements Serializable {

    private String name;
    private String phoneNumber;

}
