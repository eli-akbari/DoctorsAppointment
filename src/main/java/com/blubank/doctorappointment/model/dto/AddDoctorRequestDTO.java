package com.blubank.doctorappointment.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddDoctorRequestDTO implements Serializable {

    private String name;
}
