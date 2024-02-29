package com.blubank.doctorappointment.controller;

import com.blubank.doctorappointment.exception.ApiResponse;
import com.blubank.doctorappointment.exception.ResponseStatus;
import com.blubank.doctorappointment.model.dto.AddDoctorRequestDTO;
import com.blubank.doctorappointment.model.dto.AddDoctorResponseDTO;
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
    public ResponseEntity<ApiResponse<AddDoctorResponseDTO>> addNewDoctor(@RequestBody AddDoctorRequestDTO addDoctorRequestDTO) {
        AddDoctorResponseDTO addedDoctor = doctorService.addNewDoctor(addDoctorRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(ResponseStatus.SUCCESS,addedDoctor));
    }

}
