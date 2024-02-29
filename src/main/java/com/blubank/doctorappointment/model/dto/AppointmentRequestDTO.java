package com.blubank.doctorappointment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequestDTO implements Serializable {

    @NotNull
    private LocalDateTime startTime;
    @NotNull
    private LocalDateTime endTime;
}
