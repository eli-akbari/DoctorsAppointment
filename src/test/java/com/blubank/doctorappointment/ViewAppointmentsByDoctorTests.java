package com.blubank.doctorappointment;

import com.blubank.doctorappointment.model.AppointmentStatus;
import com.blubank.doctorappointment.model.dto.AppointmentDTO;
import com.blubank.doctorappointment.model.entity.AppointmentEntity;
import com.blubank.doctorappointment.model.entity.DoctorEntity;
import com.blubank.doctorappointment.model.entity.PatientEntity;
import com.blubank.doctorappointment.repository.AppointmentRepository;
import com.blubank.doctorappointment.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ViewAppointmentsByDoctorTests {

    @Mock
    private AppointmentRepository appointmentRepository;
    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    public void testNoAppointmentsAndEmptyListReturned() {

        Long doctorId = 1L;
        when(appointmentRepository.findAppointmentEntitiesByDoctor_Id(doctorId)).thenReturn(new ArrayList<>());

        List<AppointmentDTO> appointments = appointmentService.getDoctorAppointments(doctorId).getAppointmentList();

        assertNotNull(appointments);
        assertTrue(appointments.isEmpty());
    }

    @Test
    public void testGetTakenAppointmentsIncludePatientDetails() {
        List<AppointmentEntity> appointmentEntities = new ArrayList<>();
        DoctorEntity doctor = new DoctorEntity();
        Long doctorId = 1L;
        doctor.setId(doctorId);

        AppointmentEntity openAppointment = new AppointmentEntity();
        openAppointment.setId(1L);
        openAppointment.setStartTime(LocalDateTime.of(2024, 3, 1, 9, 0));
        openAppointment.setEndTime(LocalDateTime.of(2024, 3, 1, 9, 30));
        openAppointment.setStatus(AppointmentStatus.AVAILABLE);
        openAppointment.setDoctor(doctor);

        appointmentEntities.add(openAppointment);

        PatientEntity patient = new PatientEntity();
        patient.setId(1L);
        patient.setName("Eli");
        patient.setPhoneNumber("09129570945");

        AppointmentEntity takenAppointment = new AppointmentEntity();
        takenAppointment.setId(2L);
        takenAppointment.setStartTime(LocalDateTime.of(2024, 3, 1, 10, 0));
        takenAppointment.setEndTime(LocalDateTime.of(2024, 3, 1, 10, 30));
        takenAppointment.setStatus(AppointmentStatus.TAKEN);
        takenAppointment.setDoctor(doctor);
        takenAppointment.setPatient(patient);
        appointmentEntities.add(takenAppointment);

        when(appointmentRepository.findAppointmentEntitiesByDoctor_Id(doctorId)).thenReturn(appointmentEntities);

        List<AppointmentDTO> appointments = appointmentService.getDoctorAppointments(doctorId).getAppointmentList();

        assertNotNull(appointments);
        assertEquals(2, appointments.size());
        assertEquals(AppointmentStatus.AVAILABLE, appointments.get(0).getStatus());
        assertNull(appointments.get(0).getPatientName());
        assertNull(appointments.get(0).getPatientPhoneNumber());
        assertEquals(AppointmentStatus.TAKEN, appointments.get(1).getStatus());
        assertEquals("Eli", appointments.get(1).getPatientName());
        assertEquals("09129570945", appointments.get(1).getPatientPhoneNumber());
    }
}
