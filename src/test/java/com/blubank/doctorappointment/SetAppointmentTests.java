package com.blubank.doctorappointment;

import com.blubank.doctorappointment.exception.AppointmentDurationTooShortException;
import com.blubank.doctorappointment.exception.EndTimeBeforeStartTimeException;
import com.blubank.doctorappointment.model.dto.AppointmentRequestDTO;
import com.blubank.doctorappointment.model.entity.DoctorEntity;
import com.blubank.doctorappointment.repository.DoctorRepository;
import com.blubank.doctorappointment.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SetAppointmentTests {

    @Mock
    private DoctorRepository doctorRepository;
    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    public void testEndTimeBeforeStartTime() {
        LocalDateTime startTime = LocalDateTime.of(2024, 3, 1, 17, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 3, 1, 9, 0, 0);
        DoctorEntity doctor = new DoctorEntity();
        when(doctorRepository.findById(anyLong())).thenReturn(java.util.Optional.of(doctor));

        assertThrows(EndTimeBeforeStartTimeException.class,
                () -> appointmentService.setAppointmentsByDoctor(new AppointmentRequestDTO(startTime, endTime), 1L));
    }
    @Test
    public void testAppointmentDurationTooShort() {
        LocalDateTime startTime = LocalDateTime.of(2024, 3, 1, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 3, 1, 9, 20);
        DoctorEntity doctor = new DoctorEntity();
        when(doctorRepository.findById(anyLong())).thenReturn(java.util.Optional.of(doctor));

        assertThrows(AppointmentDurationTooShortException.class,
                () -> appointmentService.setAppointmentsByDoctor(new AppointmentRequestDTO(startTime, endTime), 1L));
    }
}
