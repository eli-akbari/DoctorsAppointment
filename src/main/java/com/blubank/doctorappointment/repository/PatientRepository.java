package com.blubank.doctorappointment.repository;

import com.blubank.doctorappointment.model.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, Long> {

    Optional<PatientEntity> findPatientEntityByNameAndPhoneNumber(String patientName, String patientPhoneNumber);
}
