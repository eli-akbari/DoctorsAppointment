package com.blubank.doctorappointment.model.mapper;

import com.blubank.doctorappointment.model.dto.DoctorRequestDTO;
import com.blubank.doctorappointment.model.dto.DoctorResponseDTO;
import com.blubank.doctorappointment.model.entity.DoctorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    DoctorMapper INSTANCE = Mappers.getMapper(DoctorMapper.class);

    @Mapping(target = "id", ignore = true)
    DoctorEntity toDoctorEntity(DoctorRequestDTO doctorRequestDTO);

    DoctorResponseDTO toDoctorRsDto(DoctorEntity doctorEntity);
}
