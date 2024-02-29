package com.blubank.doctorappointment;

import com.blubank.doctorappointment.exception.AppointmentNotFoundException;
import com.blubank.doctorappointment.exception.TakenAppointmentException;
import com.blubank.doctorappointment.model.AppointmentStatus;
import com.blubank.doctorappointment.model.dto.TakeAppointmentRequestDTO;
import com.blubank.doctorappointment.model.entity.AppointmentEntity;
import com.blubank.doctorappointment.repository.AppointmentRepository;
import com.blubank.doctorappointment.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DeleteOpenAppointmentTests {

    @Mock
    private AppointmentRepository appointmentRepository;
    @InjectMocks
    private AppointmentService appointmentService;


    @Test
    public void testDeleteNoOpenAppointmentNotFoundError() {
        Long appointmentId = 1L;
        Long doctorId = 1L;
        when(appointmentRepository.findAppointmentEntityByIdAndDoctor_Id(appointmentId, doctorId)).thenReturn(java.util.Optional.empty());

        assertThrows(AppointmentNotFoundException.class, () -> appointmentService.deleteOpenAppointment(appointmentId, doctorId));
    }

    @Test
    public void testDeleteTakenAppointmentError() {
        Long appointmentId = 1L;
        Long doctorId = 1L;
        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setStatus(AppointmentStatus.TAKEN);
        when(appointmentRepository.findAppointmentEntityByIdAndDoctor_Id(appointmentId, doctorId)).thenReturn(java.util.Optional.of(appointmentEntity));

        assertThrows(TakenAppointmentException.class, () -> appointmentService.deleteOpenAppointment(appointmentId, doctorId));
    }

    @Test
    public void testConcurrentAppointmentDeletionAndTaking() throws InterruptedException {
        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setId(1L);
        appointmentEntity.setStatus(AppointmentStatus.AVAILABLE);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointmentEntity));

        CountDownLatch latch = new CountDownLatch(2);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(() -> {
            try {
                appointmentService.deleteOpenAppointment(1L,1L);
            } catch (Exception e) {

            } finally {
                latch.countDown();
            }
        });

        executorService.execute(() -> {
            try {
                appointmentService.takeOpenAppointment(createMockTakeAppointmentRequestDTO(1L));
            } catch (Exception e) {

            } finally {
                latch.countDown();
            }
        });
        latch.await();

        verify(appointmentRepository, atMostOnce()).deleteById(1L);
        verify(appointmentRepository, never()).save(any(AppointmentEntity.class));

        executorService.shutdown();
    }

    private TakeAppointmentRequestDTO createMockTakeAppointmentRequestDTO(Long appointmentId) {
        TakeAppointmentRequestDTO requestDTO = new TakeAppointmentRequestDTO();
        requestDTO.setAppointmentId(appointmentId);
        requestDTO.setPatientName("Eli");
        requestDTO.setPhoneNumber("09129570945");
        return requestDTO;
    }
}
