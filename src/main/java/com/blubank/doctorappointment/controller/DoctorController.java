package com.blubank.doctorappointment.controller;

import com.blubank.doctorappointment.exception.ApiResponse;
import com.blubank.doctorappointment.exception.ResponseStatus;
import com.blubank.doctorappointment.model.dto.DoctorRequestDTO;
import com.blubank.doctorappointment.model.dto.DoctorResponseDTO;
import com.blubank.doctorappointment.service.DoctorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doctor")
@AllArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping()
    public ResponseEntity<ApiResponse<DoctorResponseDTO>> addNewDoctor(@RequestBody DoctorRequestDTO doctorRequestDTO) {
        DoctorResponseDTO addedDoctor = doctorService.addNewDoctor(doctorRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(ResponseStatus.SUCCESS,addedDoctor));
    }

}
