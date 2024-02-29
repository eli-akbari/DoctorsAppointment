package com.blubank.doctorappointment.model.mapper;

import com.blubank.doctorappointment.model.dto.AddPatientRequestDTO;
import com.blubank.doctorappointment.model.dto.AddPatientResponseDTO;
import com.blubank.doctorappointment.model.entity.PatientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientMapper INSTANCE = Mappers.getMapper(PatientMapper.class);

    @Mapping(target = "id", ignore = true)
    PatientEntity toPatientEntity(AddPatientRequestDTO addPatientRequestDTO);

    AddPatientResponseDTO toPatientRsDto(PatientEntity patientEntity);
}
