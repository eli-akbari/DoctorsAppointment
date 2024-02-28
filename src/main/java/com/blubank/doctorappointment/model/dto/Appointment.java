package com.blubank.doctorappointment.model.dto;

import com.blubank.doctorappointment.model.AppointmentStatus;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Appointment implements Serializable {

    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private AppointmentStatus status;
    private String doctorName;
    private String patientName;

}
