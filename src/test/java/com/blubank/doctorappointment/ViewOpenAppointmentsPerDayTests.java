package com.blubank.doctorappointment;

import com.blubank.doctorappointment.model.dto.AppointmentDTO;
import com.blubank.doctorappointment.model.dto.AppointmentPerDayRequestDTO;
import com.blubank.doctorappointment.repository.AppointmentRepository;
import com.blubank.doctorappointment.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ViewOpenAppointmentsPerDayTests {

    @Mock
    private AppointmentRepository appointmentRepository;
    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    public void testGetOpenAppointmentsForDayNoOpenAppointmentsEmptyListReturned() {
        Long doctorId = 1L;
        LocalDate date = LocalDate.of(2024, 3, 1);
        when(appointmentRepository.findAppointmentEntitiesByDoctor_IdAndStartTimeBetween(doctorId, date.atStartOfDay(), date.atTime(23, 59, 59)))
                .thenReturn(new ArrayList<>());

        List<AppointmentDTO> openAppointments = appointmentService.getOpenAppointmentsForDay(doctorId, new AppointmentPerDayRequestDTO(date)).getAppointmentList();

        assertNotNull(openAppointments);
        assertTrue(openAppointments.isEmpty());
    }
}
