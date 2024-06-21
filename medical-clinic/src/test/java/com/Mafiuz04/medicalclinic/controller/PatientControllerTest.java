package com.Mafiuz04.medicalclinic.controller;

import com.Mafiuz04.medicalclinic.mapper.PatientMapper;
import com.Mafiuz04.medicalclinic.model.*;
import com.Mafiuz04.medicalclinic.service.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PatientMapper patientMapper;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PatientService patientService;

    @Test
    void getPatients_PatientsExist_ReturnPatients() throws Exception {
        when(patientService.getPatients(any()))
                .thenReturn(List.of(
                        new PatientDto(1L, LocalDate.of(1999, 12, 12), new ArrayList<>(), new MedicalUser(1L, "Marek", "mama", "sadasd", "sadas")),
                        new PatientDto(2L, LocalDate.of(2000, 11, 11), new ArrayList<>(), new MedicalUser(2L, "Marek", "mama", "sadasd", "sadas")))
                );
        mockMvc.perform(MockMvcRequestBuilders.get("/patients").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[0].birthday").value("1999-12-12"))
                .andExpect(jsonPath("$[1].birthday").value("2000-11-11"))
                .andExpect(jsonPath("$[0].appointments").value(new ArrayList<>()))
                .andExpect(jsonPath("$[1].appointments").value(new ArrayList<>()))
                .andExpect(jsonPath("$[0].medicalUser.id").value(1L))
                .andExpect(jsonPath("$[1].medicalUser.id").value(2L));
    }

    @Test
    void getPatientById_PatientExist_ReturnPatient() throws Exception {
        Long patientId = 1L;
        when(patientService.getPatientById(patientId))
                .thenReturn(
                        new PatientDto(1L, LocalDate.of(1999, 12, 12),
                                new ArrayList<>(),
                                new MedicalUser(1L, "Marek", "mama", "sadasd", "sadas")));

        mockMvc.perform(MockMvcRequestBuilders.get("/patients/1").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.birthday").value("1999-12-12"))
                .andExpect(jsonPath("$.appointments").value(new ArrayList<>()))
                .andExpect(jsonPath("$.medicalUser.id").value(1));
    }

    @Test
    void addPatient_PatientAdded_ReturnPatientDto() throws Exception {
        PatientCreateDto patientCreateDto = new PatientCreateDto("sadasd", "sda", LocalDate.of(1999, 12, 12),
                new MedicalUser(1L, "Marek", "mama", "sadasd", "sadas"));
        Patient patient = patientMapper.createToPatient(patientCreateDto);
        patient.setId(1L);
        patient.setAppointments(new ArrayList<>());
        PatientDto dto = patientMapper.toDto(patient);
        when(patientService.addPatient(patientCreateDto)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/patients").content(objectMapper.writeValueAsString(patientCreateDto)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.birthday").value("1999-12-12"))
                .andExpect(jsonPath("$.appointments").value(new ArrayList<>()))
                .andExpect(jsonPath("$.medicalUser.id").value(1))
                .andExpect(jsonPath("$.medicalUser.firstName").value("Marek"))
                .andExpect(jsonPath("$.medicalUser.lastName").value("mama"));
    }

    @Test
    void deletePatientById_PatientDeleted() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/patients/1").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void updatePatient_PatientUpdated_ReturnPatientDto() throws Exception {
        PatientCreateDto patientCreateDto = new PatientCreateDto("123123", "saasdsad", LocalDate.of(1999, 12, 12),
                new MedicalUser(1L, "asdasd", "saadas", "sadas@", "sadasd"));
        Patient patient = patientMapper.createToPatient(patientCreateDto);
        patient.setId(1L);
        patient.setAppointments(new ArrayList<>());
        PatientDto dto = patientMapper.toDto(patient);
        when(patientService.updatePatient(patient.getId(), patientCreateDto)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.put("/patients/1").content(objectMapper.writeValueAsString(patientCreateDto)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.medicalUser.id").value(1))
                .andExpect(jsonPath("$.medicalUser.firstName").value("asdasd"))
                .andExpect(jsonPath("$.medicalUser.lastName").value("saadas"));
    }
}
