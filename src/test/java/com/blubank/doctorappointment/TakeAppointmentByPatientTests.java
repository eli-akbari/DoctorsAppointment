package com.blubank.doctorappointment;

import com.blubank.doctorappointment.exception.MissingPatientInfoException;
import com.blubank.doctorappointment.exception.TakenAppointmentException;
import com.blubank.doctorappointment.model.AppointmentStatus;
import com.blubank.doctorappointment.model.dto.TakeAppointmentRequestDTO;
import com.blubank.doctorappointment.model.entity.AppointmentEntity;
import com.blubank.doctorappointment.model.entity.PatientEntity;
import com.blubank.doctorappointment.repository.AppointmentRepository;
import com.blubank.doctorappointment.repository.DoctorRepository;
import com.blubank.doctorappointment.repository.PatientRepository;
import com.blubank.doctorappointment.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TakeAppointmentByPatientTests {

    @Mock
    private PatientRepository patientRepository;
    @Mock
    private AppointmentRepository appointmentRepository;
    @InjectMocks
    private AppointmentService appointmentService;


    @Test
    public void testTakeOpenAppointmentMissingPatientInfo() {
        TakeAppointmentRequestDTO requestDTO = new TakeAppointmentRequestDTO(
                1L, "Eli", "");

        assertThrows(MissingPatientInfoException.class, () -> appointmentService.takeOpenAppointment(requestDTO));
    }

    @Test
    public void testTakeOpenAppointmentAppointmentAlreadyTaken() {
        TakeAppointmentRequestDTO requestDTO = new TakeAppointmentRequestDTO(1L, "Eli", "1234567890");
        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setStatus(AppointmentStatus.TAKEN);
        when(appointmentRepository.findById(1L)).thenReturn(java.util.Optional.of(appointmentEntity));

        assertThrows(TakenAppointmentException.class, () ->
                appointmentService.takeOpenAppointment(requestDTO)
        );
    }

    @Test
    public void testTakeOpenAppointmentSimultaneous() throws InterruptedException {
        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setId(1L);
        appointmentEntity.setStatus(AppointmentStatus.AVAILABLE);

        PatientEntity patientEntity = new PatientEntity();
        patientEntity.setName("Eli");
        patientEntity.setPhoneNumber("09129570945");

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointmentEntity));
        when(patientRepository.findPatientEntityByNameAndPhoneNumber("Eli", "09129570945")).thenReturn(Optional.of(patientEntity));

        CountDownLatch latch = new CountDownLatch(2);

        Runnable appointmentTask = () -> {
        try {
            appointmentService.takeOpenAppointment(new TakeAppointmentRequestDTO(1L, "Eli", "09129570945"));
        } catch (Exception e) {
        } finally {
            latch.countDown();
        }
    };
        Thread thread1 = new Thread(appointmentTask);
        Thread thread2 = new Thread(appointmentTask);
        thread1.start();
        thread2.start();

        latch.await();

    assertThrows(TakenAppointmentException.class, () -> {
        appointmentService.takeOpenAppointment(new TakeAppointmentRequestDTO(1L, "Eli", "09129570945"));
    });
}
}
