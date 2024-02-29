package com.blubank.doctorappointment.controller;

import com.blubank.doctorappointment.exception.ApiResponse;
import com.blubank.doctorappointment.exception.ResponseStatus;
import com.blubank.doctorappointment.model.dto.AddPatientRequestDTO;
import com.blubank.doctorappointment.model.dto.AddPatientResponseDTO;
import com.blubank.doctorappointment.service.PatientService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patient")
@AllArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping()
    public ResponseEntity<ApiResponse<AddPatientResponseDTO>> addNewPatient(@RequestBody AddPatientRequestDTO addPatientRequestDTO) {
        AddPatientResponseDTO addedPatient = patientService.addNewPatient(addPatientRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(ResponseStatus.SUCCESS,addedPatient));
    }
}
