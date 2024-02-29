package com.blubank.doctorappointment.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddPatientResponseDTO implements Serializable {

    private Long id;
    private String name;
    private String phoneNumber;
}
