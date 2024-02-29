package com.blubank.doctorappointment.model.entity;

import com.blubank.doctorappointment.model.AppointmentStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointment")
@Getter
@Setter
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APPOINTMENT_ID")
    private Long id;

    @Column(name = "START_TIME")
    private LocalDateTime startTime;

    @Column(name = "END_TIME")
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "DOCTOR_ID", nullable = false)
    private DoctorEntity doctor;

    @ManyToOne
    @JoinColumn(name = "PATIENT_ID")
    private PatientEntity patient;

    @Column(name = "STATUS")
    private AppointmentStatus status;

    @Column(name = "PATIENT_PHONE_NUMBER")
    private String patientPhoneNumber;

    @Version
    private int version;

}
