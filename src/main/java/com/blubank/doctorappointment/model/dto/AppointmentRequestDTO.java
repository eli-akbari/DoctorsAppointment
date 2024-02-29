package com.blubank.doctorappointment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequestDTO implements Serializable {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
