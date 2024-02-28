package com.blubank.doctorappointment;

import com.blubank.doctorappointment.exception.AppointmentDurationTooShortException;
import com.blubank.doctorappointment.exception.EndTimeBeforeStartTimeException;
import com.blubank.doctorappointment.model.AppointmentStatus;
import com.blubank.doctorappointment.model.dto.Appointment;
import com.blubank.doctorappointment.model.dto.SetAppointmentRqDTO;
import com.blubank.doctorappointment.model.entity.AppointmentEntity;
import com.blubank.doctorappointment.model.entity.DoctorEntity;
import com.blubank.doctorappointment.model.entity.PatientEntity;
import com.blubank.doctorappointment.repository.AppointmentRepository;
import com.blubank.doctorappointment.repository.DoctorRepository;
import com.blubank.doctorappointment.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AppointmentServiceTests {
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private AppointmentRepository appointmentRepository;
    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    public void testEndTimeBeforeStartTime() {
        LocalDateTime startTime = LocalDateTime.of(2024, 3, 1, 17, 0,0);
        LocalDateTime endTime = LocalDateTime.of(2024, 3, 1, 9, 0,0);
        DoctorEntity doctor = new DoctorEntity();
        when(doctorRepository.findById(anyLong())).thenReturn(java.util.Optional.of(doctor));

        EndTimeBeforeStartTimeException exception = assertThrows(EndTimeBeforeStartTimeException.class,
                () -> appointmentService.setAppointmentsByDoctor(new SetAppointmentRqDTO(startTime, endTime), 1L));
        assertEquals("End time cannot be before start time", exception.getMessage());
    }

    @Test
    public void testAppointmentDurationTooShort() {
        LocalDateTime startTime = LocalDateTime.of(2024, 3, 1, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 3, 1, 9, 29);
        DoctorEntity doctor = new DoctorEntity();
        when(doctorRepository.findById(anyLong())).thenReturn(java.util.Optional.of(doctor));

        AppointmentDurationTooShortException exception = assertThrows(AppointmentDurationTooShortException.class,
                () -> appointmentService.setAppointmentsByDoctor(new SetAppointmentRqDTO(startTime, endTime), 1L));
        assertEquals("Appointment duration should be at least 30 minutes", exception.getMessage());
    }

    @Test
    public void testNoAppointmentsEmptyListReturned() {

        Long doctorId = 1L;
        when(appointmentRepository.findAppointmentEntitiesByDoctor_Id(doctorId)).thenReturn(new ArrayList<>());

        List<Appointment> appointments = appointmentService.getDoctorAppointments(doctorId).getAppointmentList();

        assertNotNull(appointments);
        assertTrue(appointments.isEmpty());
    }


    @Test
    public void testGetDoctorAppointments_AppointmentsExist_TakenAppointmentsIncludePatientDetails() {
        // Arrange
        Long doctorId = 1L;
        List<AppointmentEntity> appointmentEntities = new ArrayList<>();
        DoctorEntity doctor = new DoctorEntity();
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
        patient.setName("John Doe");
        patient.setPhoneNumber("1234567890");

        AppointmentEntity takenAppointment = new AppointmentEntity();
        takenAppointment.setId(2L);
        takenAppointment.setStartTime(LocalDateTime.of(2024, 3, 1, 10, 0));
        takenAppointment.setEndTime(LocalDateTime.of(2024, 3, 1, 10, 30));
        takenAppointment.setStatus(AppointmentStatus.TAKEN);
        takenAppointment.setDoctor(doctor);
        takenAppointment.setPatient(patient); // Assigning the patient retrieved from repository
        appointmentEntities.add(takenAppointment);

        when(appointmentRepository.findAppointmentEntitiesByDoctor_Id(doctorId)).thenReturn(appointmentEntities);

        List<Appointment> appointments = appointmentService.getDoctorAppointments(doctorId).getAppointmentList();

        assertNotNull(appointments);
        assertEquals(2, appointments.size());
        assertEquals(AppointmentStatus.AVAILABLE, appointments.get(0).getStatus());
        assertNull(appointments.get(0).getPatientName());
        assertNull(appointments.get(0).getPatientPhoneNumber());
        assertEquals(AppointmentStatus.TAKEN, appointments.get(1).getStatus());
        assertEquals("John Doe", appointments.get(1).getPatientName());
        assertEquals("1234567890", appointments.get(1).getPatientPhoneNumber());
    }

}
