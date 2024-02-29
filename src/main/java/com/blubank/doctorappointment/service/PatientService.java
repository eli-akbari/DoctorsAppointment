package com.blubank.doctorappointment.service;

import com.blubank.doctorappointment.model.dto.AddPatientRequestDTO;
import com.blubank.doctorappointment.model.dto.AddPatientResponseDTO;
import com.blubank.doctorappointment.model.entity.PatientEntity;
import com.blubank.doctorappointment.model.mapper.PatientMapper;
import com.blubank.doctorappointment.repository.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public AddPatientResponseDTO addNewPatient(AddPatientRequestDTO addPatientRequestDTO) {
        PatientEntity patientEntity = PatientMapper.INSTANCE.toPatientEntity(addPatientRequestDTO);

        PatientEntity savedPatient = patientRepository.save(patientEntity);
        return PatientMapper.INSTANCE.toPatientRsDto(savedPatient);
    }
}
