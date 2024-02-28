package com.blubank.doctorappointment.service;

import com.blubank.doctorappointment.exception.*;
import com.blubank.doctorappointment.model.AppointmentStatus;
import com.blubank.doctorappointment.model.dto.*;
import com.blubank.doctorappointment.model.entity.AppointmentEntity;
import com.blubank.doctorappointment.model.entity.DoctorEntity;
import com.blubank.doctorappointment.model.entity.PatientEntity;
import com.blubank.doctorappointment.model.mapper.AppointmentMapper;
import com.blubank.doctorappointment.repository.AppointmentRepository;
import com.blubank.doctorappointment.repository.DoctorRepository;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AppointmentService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;


    public AppointmentResponseDTO setAppointmentsByDoctor(AppointmentRequestDTO appointmentRqDTO, Long doctorId) {
        LocalDateTime startTime = appointmentRqDTO.getStartTime();
        LocalDateTime endTime = appointmentRqDTO.getEndTime();

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
        AppointmentResponseDTO result = new AppointmentResponseDTO();
        result.setAppointmentList(AppointmentMapper.INSTANCE.toAppointments(appointmentEntities));
        return result;
    }

    public AppointmentResponseDTO getDoctorAppointments(Long doctorId) {
        List<AppointmentEntity> appointmentEntities = appointmentRepository.findAppointmentEntitiesByDoctor_Id(doctorId);

        List<AppointmentDTO> appointments = new ArrayList<>();
        for (AppointmentEntity appointmentEntity : appointmentEntities) {
            AppointmentDTO appointment = AppointmentMapper.INSTANCE.toAppointmentDto(appointmentEntity);

            if (appointmentEntity.getStatus() == AppointmentStatus.TAKEN) {
                appointment.setPatientName(appointmentEntity.getPatient().getName());
                appointment.setPatientPhoneNumber(appointmentEntity.getPatient().getPhoneNumber());
            }
            appointments.add(appointment);
        }
        AppointmentResponseDTO result = new AppointmentResponseDTO();
        result.setAppointmentList(appointments);
        return result;
    }

    public void deleteOpenAppointment(Long appointmentId, Long doctorId) {
        AppointmentEntity appointmentEntity = appointmentRepository.findAppointmentEntityByIdAndDoctor_Id(appointmentId, doctorId)
                .orElseThrow(AppointmentNotFoundException::new);

        if (appointmentEntity.getStatus() != AppointmentStatus.AVAILABLE) {
            throw new TakenAppointmentException();
        }
        try {
            appointmentEntity.setStatus(AppointmentStatus.AVAILABLE);
            appointmentRepository.save(appointmentEntity);
            appointmentRepository.delete(appointmentEntity);

        } catch (OptimisticLockingFailureException ex) {
            throw new ConflictException();
        }
    }

    public AppointmentResponseDTO getOpenAppointmentsForDay(Long doctorId, AppointmentPerDayRequestDTO appointmentPerDayRequestDTO) {
        List<AppointmentEntity> appointmentEntities = appointmentRepository.
                findAppointmentEntitiesByDoctor_IdAndStartTimeBetween(doctorId, appointmentPerDayRequestDTO.getDay().atStartOfDay(), appointmentPerDayRequestDTO.getDay().atTime(23, 59, 59));

        List<AppointmentDTO> appointments = appointmentEntities.stream()
                .filter(appointmentEntity -> appointmentEntity.getStatus() == AppointmentStatus.AVAILABLE)
                .map(AppointmentMapper.INSTANCE::toAppointmentDto)
                .collect(Collectors.toList());

        AppointmentResponseDTO appointmentRsDTO = new AppointmentResponseDTO();
        appointmentRsDTO.setAppointmentList(appointments);
        return appointmentRsDTO;
    }

    @Transactional
    public void takeOpenAppointment(TakeAppointmentRequestDTO takeAppointmentRequestDTO) {
        String patientName = takeAppointmentRequestDTO.getPatientName();
        String patientPhoneNumber = takeAppointmentRequestDTO.getPhoneNumber();
        Long appointmentId = takeAppointmentRequestDTO.getAppointmentId();

        if (StringUtils.isBlank(patientName) || StringUtils.isBlank(patientPhoneNumber)) {
            throw new MissingPatientInfoException();
        }

        Optional<AppointmentEntity> appointmentEntity = appointmentRepository.findById(appointmentId);
        if (appointmentEntity.isEmpty()) {
           throw new AppointmentNotFoundException();
        }

            synchronized (this) {
            if (appointmentEntity.get().getStatus() != AppointmentStatus.AVAILABLE) {
                throw new TakenAppointmentException();
            }

            PatientEntity patientEntity = appointmentEntity.get().getPatient();
            patientEntity.setName(takeAppointmentRequestDTO.getPatientName());
            patientEntity.setPhoneNumber(takeAppointmentRequestDTO.getPhoneNumber());

            appointmentEntity.get().setStatus(AppointmentStatus.TAKEN);
                try {
                    appointmentRepository.save(appointmentEntity.get());
                } catch (OptimisticLockingFailureException ex) {
                    throw new ConflictException();
                }
        }
    }

    public AppointmentResponseDTO getAppointmentByPhoneNumber(String phoneNumber) {
            List<AppointmentEntity> appointments = appointmentRepository.findAppointmentEntityByPatientPhoneNumber(phoneNumber);
            List<AppointmentDTO> appointmentDTOs = appointments.stream()
                    .map(AppointmentMapper.INSTANCE::toAppointmentDto)
                    .collect(Collectors.toList());

            AppointmentResponseDTO responseDTO = new AppointmentResponseDTO();
            responseDTO.setAppointmentList(appointmentDTOs);
            return responseDTO;
    }
}
