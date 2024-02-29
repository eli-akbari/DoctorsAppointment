package com.blubank.doctorappointment.controller;

import com.blubank.doctorappointment.exception.ApiResponse;
import com.blubank.doctorappointment.exception.ResponseStatus;
import com.blubank.doctorappointment.model.dto.*;
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

    @PostMapping("/{doctorId}")
    public ResponseEntity<ApiResponse<AppointmentResponseDTO>> setAppointmentsByDoctor(@PathVariable Long doctorId, @RequestBody AppointmentRequestDTO setAppointmentRequest) {
        AppointmentResponseDTO result = appointmentService.setAppointmentsByDoctor(setAppointmentRequest,doctorId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(ResponseStatus.SUCCESS,result));
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<ApiResponse<AppointmentResponseDTO>> getDoctorAppointments(@PathVariable Long doctorId) {
        AppointmentResponseDTO result = appointmentService.getDoctorAppointments(doctorId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(ResponseStatus.SUCCESS,result));
    }

    @DeleteMapping("/{doctorId}/{appointmentId}")
    public ResponseEntity<ApiResponse<Void>> deleteOpenAppointmentByDoctor(@PathVariable Long doctorId,@PathVariable Long appointmentId) {
        appointmentService.deleteOpenAppointment(appointmentId,doctorId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(ResponseStatus.SUCCESS,null));
    }

    @PostMapping("/perDay/{doctorId}")
    public ResponseEntity<ApiResponse<AppointmentResponseDTO>> getOpenAppointmentsForDay(@PathVariable Long doctorId, @RequestBody AppointmentPerDayRequestDTO appointmentPerDayRequest) {
        AppointmentResponseDTO result = appointmentService.getOpenAppointmentsForDay(doctorId,appointmentPerDayRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(ResponseStatus.SUCCESS,result));
    }

    @PostMapping("/take")
    public ResponseEntity<ApiResponse<TakeAppointmentResponseDTO>> takeOpenAppointment(@RequestBody TakeAppointmentRequestDTO takeAppointmentRequestDTO) {
        TakeAppointmentResponseDTO takenAppointment = appointmentService.takeOpenAppointment(takeAppointmentRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(ResponseStatus.SUCCESS,takenAppointment));
    }

    @GetMapping("/{doctorId}/{phoneNumber}")
    public ResponseEntity<ApiResponse<AppointmentResponseDTO>> getPatientAppointmentsByPhoneNumber(@PathVariable Long doctorId
            ,@PathVariable String phoneNumber) {
        AppointmentResponseDTO result = appointmentService.getPatientAppointmentByPatientPhoneNumber(doctorId,phoneNumber);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(ResponseStatus.SUCCESS,result));
    }

}
