package com.blubank.doctorappointment.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class AddDoctorRequestDTO implements Serializable {

    @NotNull
    private String name;
}
