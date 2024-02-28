package com.blubank.doctorappointment;

import com.blubank.doctorappointment.exception.AppointmentDurationTooShortException;
import com.blubank.doctorappointment.exception.EndTimeBeforeStartTimeException;
import com.blubank.doctorappointment.model.dto.SetAppointmentRqDTO;
import com.blubank.doctorappointment.model.entity.DoctorEntity;
import com.blubank.doctorappointment.repository.AppointmentRepository;
import com.blubank.doctorappointment.repository.DoctorRepository;
import com.blubank.doctorappointment.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AppointmentServiceTests {
    @Mock
    private DoctorRepository doctorRepository;
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

}
