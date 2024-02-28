package com.blubank.doctorappointment;

import com.blubank.doctorappointment.exception.*;
import com.blubank.doctorappointment.model.AppointmentStatus;
import com.blubank.doctorappointment.model.dto.*;
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
import org.springframework.dao.OptimisticLockingFailureException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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
        LocalDateTime startTime = LocalDateTime.of(2024, 3, 1, 17, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 3, 1, 9, 0, 0);
        DoctorEntity doctor = new DoctorEntity();
        when(doctorRepository.findById(anyLong())).thenReturn(java.util.Optional.of(doctor));

        EndTimeBeforeStartTimeException exception = assertThrows(EndTimeBeforeStartTimeException.class,
                () -> appointmentService.setAppointmentsByDoctor(new AppointmentRequestDTO(startTime, endTime), 1L));
        assertEquals("End time cannot be before start time", exception.getMessage());
    }

    @Test
    public void testAppointmentDurationTooShort() {
        LocalDateTime startTime = LocalDateTime.of(2024, 3, 1, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 3, 1, 9, 29);
        DoctorEntity doctor = new DoctorEntity();
        when(doctorRepository.findById(anyLong())).thenReturn(java.util.Optional.of(doctor));

        AppointmentDurationTooShortException exception = assertThrows(AppointmentDurationTooShortException.class,
                () -> appointmentService.setAppointmentsByDoctor(new AppointmentRequestDTO(startTime, endTime), 1L));
        assertEquals("Appointment duration should be at least 30 minutes", exception.getMessage());
    }

    @Test
    public void testNoAppointmentsEmptyListReturned() {

        Long doctorId = 1L;
        when(appointmentRepository.findAppointmentEntitiesByDoctor_Id(doctorId)).thenReturn(new ArrayList<>());

        List<AppointmentDTO> appointments = appointmentService.getDoctorAppointments(doctorId).getAppointmentList();

        assertNotNull(appointments);
        assertTrue(appointments.isEmpty());
    }

    @Test
    public void testGetDoctorAppointments_AppointmentsExist_TakenAppointmentsIncludePatientDetails() {
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
        assertEquals("John Doe", appointments.get(1).getPatientName());
        assertEquals("1234567890", appointments.get(1).getPatientPhoneNumber());
    }

    @Test
    public void testDeleteOpenAppointment_NoOpenAppointment_NotFoundError() {
        Long appointmentId = 1L;
        Long doctorId = 1L;
        when(appointmentRepository.findAppointmentEntityByIdAndDoctor_Id(appointmentId, doctorId)).thenReturn(java.util.Optional.empty());

        assertThrows(AppointmentNotFoundException.class, () -> appointmentService.deleteOpenAppointment(appointmentId, doctorId));
    }

    @Test
    public void testDeleteOpenAppointment_AppointmentTaken_TakenAppointmentError() {
        Long appointmentId = 1L;
        Long doctorId = 1L;
        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setStatus(AppointmentStatus.TAKEN);
        when(appointmentRepository.findAppointmentEntityByIdAndDoctor_Id(appointmentId, doctorId)).thenReturn(java.util.Optional.of(appointmentEntity));

        assertThrows(TakenAppointmentException.class, () -> appointmentService.deleteOpenAppointment(appointmentId, doctorId));
    }

    @Test
    public void testDeleteOpenAppointment_ConcurrencyIssue_ConflictError() {
        Long appointmentId = 1L;
        Long doctorId = 1L;
        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setStatus(AppointmentStatus.AVAILABLE);
        doThrow(OptimisticLockingFailureException.class).when(appointmentRepository).delete(appointmentEntity);
        when(appointmentRepository.findAppointmentEntityByIdAndDoctor_Id(appointmentId, doctorId)).thenReturn(java.util.Optional.of(appointmentEntity));

        assertThrows(ConflictException.class, () -> appointmentService.deleteOpenAppointment(appointmentId, doctorId));
    }

    @Test
    public void testDeleteOpenAppointment_OpenAppointmentDeletedSuccessfully() {
        Long appointmentId = 1L;
        Long doctorId = 1L;
        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setStatus(AppointmentStatus.AVAILABLE);
        when(appointmentRepository.findAppointmentEntityByIdAndDoctor_Id(appointmentId, doctorId)).thenReturn(java.util.Optional.of(appointmentEntity));

        appointmentService.deleteOpenAppointment(appointmentId, doctorId);

        verify(appointmentRepository, times(1)).delete(appointmentEntity);
    }

    @Test
    public void testGetOpenAppointmentsForDay_NoOpenAppointments_EmptyListReturned() {
        Long doctorId = 1L;
        LocalDate date = LocalDate.of(2024, 3, 1);
        when(appointmentRepository.findAppointmentEntitiesByDoctor_IdAndStartTimeBetween(doctorId, date.atStartOfDay(), date.atTime(23, 59, 59)))
                .thenReturn(new ArrayList<>());

        List<AppointmentDTO> openAppointments = appointmentService.getOpenAppointmentsForDay(doctorId, new AppointmentPerDayRequestDTO(date)).getAppointmentList();

        assertNotNull(openAppointments);
        assertTrue(openAppointments.isEmpty());
    }

    @Test
    public void testTakeOpenAppointment_MissingPatientInfo_ErrorThrown() {
        TakeAppointmentRequestDTO requestDTO = new TakeAppointmentRequestDTO(
                1L, "Eli", "");

        assertThrows(MissingPatientInfoException.class, () -> appointmentService.takeOpenAppointment(requestDTO));
    }

    @Test
    public void testTakeOpenAppointment_AppointmentAlreadyTaken_ErrorThrown() {
        TakeAppointmentRequestDTO requestDTO = new TakeAppointmentRequestDTO(1L, "Eli", "1234567890");
        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setStatus(AppointmentStatus.TAKEN);
        when(appointmentRepository.findById(1L)).thenReturn(java.util.Optional.of(appointmentEntity));

        assertThrows(TakenAppointmentException.class, () ->
                appointmentService.takeOpenAppointment(requestDTO)
        );
    }


//    @Test
//    public void testConcurrentTakeOpenAppointment() throws InterruptedException {
//        // Mock appointment entity
//        AppointmentEntity appointmentEntity = new AppointmentEntity();
//        appointmentEntity.setId(1L);
//        appointmentEntity.setStatus(AppointmentStatus.AVAILABLE);
//
//        // Mock appointment request DTO
//        TakeAppointmentRequestDTO takeAppointmentRequestDTO = new TakeAppointmentRequestDTO();
//        takeAppointmentRequestDTO.setPatientName("John Doe");
//        takeAppointmentRequestDTO.setPhoneNumber("1234567890");
//        takeAppointmentRequestDTO.setAppointmentId(1L);
//
//        // Stub repository to return the mocked appointment entity
//        when(appointmentRepository.findById(1L)).thenReturn(java.util.Optional.of(appointmentEntity));
//
//        // Simulate concurrent access with multiple threads
//        Thread thread1 = new Thread(() -> {
//            try {
//                appointmentService.takeOpenAppointment(takeAppointmentRequestDTO);
//            } catch (ConflictException e) {
//            }
//        });
//
//        Thread thread2 = new Thread(() -> {
//            try {
//                appointmentService.takeOpenAppointment(takeAppointmentRequestDTO);
//            } catch (ConflictException e) {
//            }
//        });
//        thread1.start();
//        thread2.start();
//
//        thread1.join();
//        thread2.join();
//        verify(appointmentRepository, times(1)).findById(1L);
//    }

    @Test
    public void testGetAppointmentByPhoneNumber_NoAppointmentFound() {

        String phoneNumber = "1234567890";
        when(appointmentRepository.findAppointmentEntityByPatientPhoneNumber(phoneNumber))
                .thenReturn(new ArrayList<>());

        AppointmentResponseDTO responseDTO = appointmentService.getAppointmentByPhoneNumber(phoneNumber);
        assertEquals(0, responseDTO.getAppointmentList().size());
    }

    @Test
    public void testGetAppointmentByPhoneNumber_MultipleAppointmentsFound() {
        String phoneNumber = "1234567890";
        List<AppointmentEntity> appointmentEntities = new ArrayList<>();
        appointmentEntities.add(new AppointmentEntity());
        appointmentEntities.add(new AppointmentEntity());
        when(appointmentRepository.findAppointmentEntityByPatientPhoneNumber(phoneNumber))
                .thenReturn(appointmentEntities);

        AppointmentResponseDTO responseDTO = appointmentService.getAppointmentByPhoneNumber(phoneNumber);

        assertEquals(2, responseDTO.getAppointmentList().size());
    }
}


