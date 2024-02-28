package com.blubank.doctorappointment.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AppointmentResponseDTO implements Serializable {

    private List<AppointmentDTO> appointmentList;
}
