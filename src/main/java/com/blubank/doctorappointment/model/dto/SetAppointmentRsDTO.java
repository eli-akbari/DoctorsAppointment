package com.blubank.doctorappointment.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SetAppointmentRsDTO implements Serializable {

    private List<Appointment> appointmentList;
}
