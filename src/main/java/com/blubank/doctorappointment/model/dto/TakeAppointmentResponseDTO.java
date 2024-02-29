package com.blubank.doctorappointment.model.dto;

import com.blubank.doctorappointment.model.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TakeAppointmentResponseDTO implements Serializable {

    private Long appointmentId;
    private String patientName;
    private String phoneNumber;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private AppointmentStatus status;
    private String doctorName;
}
