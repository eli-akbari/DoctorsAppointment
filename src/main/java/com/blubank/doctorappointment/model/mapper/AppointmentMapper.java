package com.blubank.doctorappointment.model.mapper;

import com.blubank.doctorappointment.model.dto.Appointment;
import com.blubank.doctorappointment.model.entity.AppointmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    @Mapping(source = "doctor.name", target = "doctorName")
    @Mapping(source = "patient.name", target = "patientName")
    Appointment toAppointment(AppointmentEntity appointmentEntity);

    List<Appointment> toAppointments(List<AppointmentEntity> appointmentEntities);
}
