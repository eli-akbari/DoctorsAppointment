package com.blubank.doctorappointment.repository;

import com.blubank.doctorappointment.model.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {

    List<AppointmentEntity> findAppointmentEntitiesByDoctor_Id(Long doctorId);
}
