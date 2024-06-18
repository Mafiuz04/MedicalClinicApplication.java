package com.Mafiuz04.medicalclinic.controller;

import com.Mafiuz04.medicalclinic.mapper.AppointmentMapper;
import com.Mafiuz04.medicalclinic.mapper.InstitutionMapper;
import com.Mafiuz04.medicalclinic.model.AppointmentDto;
import com.Mafiuz04.medicalclinic.model.MedicalUser;
import com.Mafiuz04.medicalclinic.service.AppointmentService;
import com.Mafiuz04.medicalclinic.service.InstitutionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.print.attribute.standard.Media;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AppointmentControllerTest {
    @Autowired
    AppointmentMapper appointmentMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AppointmentService appointmentService;

    @Test
    void addAppointment_ReturnAppointmentDto() throws Exception {
        OngoingStubbing<AppointmentDto> appointmentDtoOngoingStubbing = when(appointmentService.createAppointment(any())).thenReturn(
                new AppointmentDto(1L, 1L, 1L, LocalDateTime.of(2025, 12, 12, 12, 12), LocalDateTime.of(2025, 12, 13, 12, 12))
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/appointments").content(objectMapper.writeValueAsString(appointmentDtoOngoingStubbing)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.doctorId").value(1))
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.startDate").value("2025-12-12T12:12:00"));
    }

    @Test
    void assignPatient_PatientExist_ReturnAppointmentDto() throws Exception {
        when(appointmentService.assignPatient(any(), any())).thenReturn(
                new AppointmentDto(1L, 1L, 1L, LocalDateTime.of(2025, 12, 12, 12, 12), LocalDateTime.of(2025, 12, 13, 12, 12))
        );

        mockMvc.perform(MockMvcRequestBuilders.patch("/appointments/1/patients/1").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId").value(1))
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.startDate").value("2025-12-12T12:12:00"));
    }

    @Test
    void getAppointments_AppointmentsExist_ReturnList() throws Exception {
        when(appointmentService.getAppointments(any())).thenReturn(List.of(
                new AppointmentDto(1L, 1L, 1L, LocalDateTime.of(2025, 12, 12, 12, 12), LocalDateTime.of(2025, 12, 13, 12, 12)),
                new AppointmentDto(2L, 2L, 2L, LocalDateTime.of(2025, 12, 12, 12, 12), LocalDateTime.of(2025, 12, 13, 12, 12)))
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/appointments").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[0].doctorId").value(1))
                .andExpect(jsonPath("$[1].doctorId").value(2))
                .andExpect(jsonPath("$[0].patientId").value(1))
                .andExpect(jsonPath("$[1].patientId").value(2));
    }
}
