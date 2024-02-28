package com.blubank.doctorappointment.repository;

import com.blubank.doctorappointment.model.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {

    List<AppointmentEntity> findAppointmentEntitiesByDoctor_Id(Long doctorId);
    List<AppointmentEntity> findAppointmentEntityByPatientPhoneNumber(String phoneNumber);
    Optional<AppointmentEntity> findAppointmentEntityByIdAndDoctor_Id(Long appointmentId, Long doctorId);
    List<AppointmentEntity> findAppointmentEntitiesByDoctor_IdAndStartTimeBetween(Long doctorId, LocalDateTime startTime, LocalDateTime endTime);
}
