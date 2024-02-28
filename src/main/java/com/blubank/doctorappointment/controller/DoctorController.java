package com.blubank.doctorappointment.controller;

import com.blubank.doctorappointment.service.DoctorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doctor")
@AllArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

}
