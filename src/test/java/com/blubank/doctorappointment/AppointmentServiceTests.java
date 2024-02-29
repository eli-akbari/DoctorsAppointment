package com.blubank.doctorappointment;

import com.blubank.doctorappointment.exception.*;
import com.blubank.doctorappointment.model.AppointmentStatus;
import com.blubank.doctorappointment.model.dto.*;
import com.blubank.doctorappointment.model.entity.AppointmentEntity;
import com.blubank.doctorappointment.repository.AppointmentRepository;
import com.blubank.doctorappointment.repository.DoctorRepository;
import com.blubank.doctorappointment.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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


