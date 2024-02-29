package com.blubank.doctorappointment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TakeAppointmentRequestDTO implements Serializable {

    @NotNull
    private Long appointmentId;
    @NotNull
    private String patientName;
    @NotNull
    private String phoneNumber;
}
