package com.blubank.doctorappointment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SetAppointmentRqDTO implements Serializable {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
