package com.blubank.doctorappointment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TakeAppointmentRequestDTO implements Serializable {

    private Long appointmentId;
    private String patientName;
    private String phoneNumber;
}
