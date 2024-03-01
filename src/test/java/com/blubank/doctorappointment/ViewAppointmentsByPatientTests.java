package com.blubank.doctorappointment;

import com.blubank.doctorappointment.model.AppointmentStatus;
import com.blubank.doctorappointment.model.dto.AppointmentDTO;
import com.blubank.doctorappointment.model.dto.AppointmentResponseDTO;
import com.blubank.doctorappointment.model.entity.AppointmentEntity;
import com.blubank.doctorappointment.repository.AppointmentRepository;
import com.blubank.doctorappointment.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ViewAppointmentsByPatientTests {

    @Mock
    private AppointmentRepository appointmentRepository;
    @InjectMocks
    private AppointmentService appointmentService;


    @Test
    public void testGetAllAppointmentsForSpecPatient() {
        Long doctorId = 1L;
        when(appointmentRepository.findAppointmentEntitiesByDoctor_IdAndPatientPhoneNumber(doctorId, "09129570945"))
                .thenReturn(Collections.emptyList());
        AppointmentResponseDTO appointmentResponseDTO = appointmentService.getPatientAppointmentByPatientPhoneNumber(doctorId,"09129570945");

        List<AppointmentDTO> appointmentList = appointmentResponseDTO.getAppointmentList();
        assertEquals(0, appointmentList.size());
    }


    @Test
    public void testGetAppointmentByPhoneNumberMultipleAppointments() {
        String phoneNumber = "09129570945";
        List<AppointmentEntity> appointmentEntities = new ArrayList<>();
        appointmentEntities.add(new AppointmentEntity());
        appointmentEntities.add(new AppointmentEntity());
        when(appointmentRepository.findAppointmentEntityByPatientPhoneNumber(phoneNumber))
                .thenReturn(appointmentEntities);

        AppointmentResponseDTO responseDTO = appointmentService.getAppointmentByPhoneNumber(phoneNumber);

        assertEquals(2, responseDTO.getAppointmentList().size());
    }
}
