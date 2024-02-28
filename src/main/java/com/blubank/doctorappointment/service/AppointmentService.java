package com.blubank.doctorappointment.service;

import com.blubank.doctorappointment.exception.AppointmentDurationTooShortException;
import com.blubank.doctorappointment.exception.DoctorDoesntExists;
import com.blubank.doctorappointment.exception.EndTimeBeforeStartTimeException;
import com.blubank.doctorappointment.model.AppointmentStatus;
import com.blubank.doctorappointment.model.dto.Appointment;
import com.blubank.doctorappointment.model.dto.SetAppointmentRqDTO;
import com.blubank.doctorappointment.model.dto.SetAppointmentRsDTO;
import com.blubank.doctorappointment.model.entity.AppointmentEntity;
import com.blubank.doctorappointment.model.entity.DoctorEntity;
import com.blubank.doctorappointment.model.mapper.AppointmentMapper;
import com.blubank.doctorappointment.repository.AppointmentRepository;
import com.blubank.doctorappointment.repository.DoctorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AppointmentService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;


    public SetAppointmentRsDTO setAppointmentsByDoctor(SetAppointmentRqDTO setAppointmentRqDTO, Long doctorId) {
        LocalDateTime startTime = setAppointmentRqDTO.getStartTime();
        LocalDateTime endTime = setAppointmentRqDTO.getEndTime();

        if (endTime.isBefore(startTime)) {
            throw new EndTimeBeforeStartTimeException();
        }

        Duration duration = Duration.between(startTime, endTime);

        if (duration.toMinutes() < 30) {
            throw new AppointmentDurationTooShortException();
        }
        DoctorEntity doctorEntity = doctorRepository.findById(doctorId).orElseThrow(DoctorDoesntExists::new);

        List<AppointmentEntity> appointmentEntities = new ArrayList<>();

        LocalDateTime currentSlot = startTime;
        while (currentSlot.plusMinutes(30).isBefore(endTime) || currentSlot.plusMinutes(30).isEqual(endTime)) {
            AppointmentEntity appointmentEntity = new AppointmentEntity();
            appointmentEntity.setStartTime(currentSlot);
            appointmentEntity.setEndTime(currentSlot.plusMinutes(30));
            appointmentEntity.setDoctor(doctorEntity);

            appointmentEntity.setStatus(AppointmentStatus.AVAILABLE);

            appointmentEntity = appointmentRepository.save(appointmentEntity);

            appointmentEntities.add(appointmentEntity);
            currentSlot = currentSlot.plusMinutes(30);
        }
        SetAppointmentRsDTO result = new SetAppointmentRsDTO();
        result.setAppointmentList(AppointmentMapper.INSTANCE.toAppointments(appointmentEntities));
        return result;
    }

    public SetAppointmentRsDTO getDoctorAppointments(Long doctorId) {
        List<AppointmentEntity> appointmentEntities = appointmentRepository.findAppointmentEntitiesByDoctor_Id(doctorId);

        List<Appointment> appointments = new ArrayList<>();
        for (AppointmentEntity appointmentEntity : appointmentEntities) {
            Appointment appointment = AppointmentMapper.INSTANCE.toAppointment(appointmentEntity);

            if (appointmentEntity.getStatus() == AppointmentStatus.TAKEN) {
                appointment.setPatientName(appointmentEntity.getPatient().getName());
                appointment.setPatientPhoneNumber(appointmentEntity.getPatient().getPhoneNumber());
            }
            appointments.add(appointment);
        }
        SetAppointmentRsDTO result = new SetAppointmentRsDTO();
        result.setAppointmentList(appointments);
        return result;
    }
}
