package com.blubank.doctorappointment.service;

import com.blubank.doctorappointment.model.dto.AddDoctorRequestDTO;
import com.blubank.doctorappointment.model.dto.AddDoctorResponseDTO;
import com.blubank.doctorappointment.model.entity.DoctorEntity;
import com.blubank.doctorappointment.model.mapper.DoctorMapper;
import com.blubank.doctorappointment.repository.DoctorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public AddDoctorResponseDTO addNewDoctor(AddDoctorRequestDTO addDoctorRequestDTO) {
        DoctorEntity doctorEntity = DoctorMapper.INSTANCE.toDoctorEntity(addDoctorRequestDTO);

        DoctorEntity savedDoctor = doctorRepository.save(doctorEntity);
        return DoctorMapper.INSTANCE.toDoctorRsDto(savedDoctor);
    }
}
