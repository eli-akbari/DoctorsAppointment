package com.blubank.doctorappointment.model.dto;

import com.blubank.doctorappointment.model.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentDTO implements Serializable {

    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private AppointmentStatus status;
    private String doctorName;
    private String patientName;
    private String patientPhoneNumber;

}
