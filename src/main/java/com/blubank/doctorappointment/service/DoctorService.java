package com.blubank.doctorappointment.service;

import com.blubank.doctorappointment.model.dto.DoctorRequestDTO;
import com.blubank.doctorappointment.model.dto.DoctorResponseDTO;
import com.blubank.doctorappointment.model.entity.DoctorEntity;
import com.blubank.doctorappointment.model.mapper.DoctorMapper;
import com.blubank.doctorappointment.repository.DoctorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorResponseDTO addNewDoctor(DoctorRequestDTO doctorRequestDTO) {

        DoctorEntity doctorEntity = DoctorMapper.INSTANCE.toDoctorEntity(doctorRequestDTO);

        DoctorEntity savedDoctor = doctorRepository.save(doctorEntity);
        return DoctorMapper.INSTANCE.toDoctorRsDto(savedDoctor);
    }
}
