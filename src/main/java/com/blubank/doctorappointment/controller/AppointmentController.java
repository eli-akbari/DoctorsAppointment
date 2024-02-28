package com.blubank.doctorappointment.controller;

import com.blubank.doctorappointment.exception.ApiResponse;
import com.blubank.doctorappointment.exception.ResponseStatus;
import com.blubank.doctorappointment.model.dto.SetAppointmentRqDTO;
import com.blubank.doctorappointment.model.dto.SetAppointmentRsDTO;
import com.blubank.doctorappointment.service.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/appointments")
@AllArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/appointments/{doctorId}")
    public ResponseEntity<ApiResponse<SetAppointmentRsDTO>> setAppointmentsByDoctor(@PathVariable Long doctorId,
                                                                                    @RequestBody SetAppointmentRqDTO setAppointmentRequest) {
        SetAppointmentRsDTO result = appointmentService.setAppointmentsByDoctor(setAppointmentRequest,doctorId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(ResponseStatus.SUCCESS,result));
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<ApiResponse<SetAppointmentRsDTO>> getDoctorAppointments(@PathVariable Long doctorId) {
        SetAppointmentRsDTO result = appointmentService.getDoctorAppointments(doctorId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(ResponseStatus.SUCCESS,result));
    }

}
