package com.Mafiuz04.medicalclinic.controller;

import com.Mafiuz04.medicalclinic.mapper.InstitutionMapper;
import com.Mafiuz04.medicalclinic.model.Institution;
import com.Mafiuz04.medicalclinic.model.InstitutionDto;
import com.Mafiuz04.medicalclinic.service.InstitutionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class InstitutionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    InstitutionMapper institutionMapper;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    InstitutionService institutionService;

    @Test
    void addInstitution_ReturnInstitutionDto() throws Exception {
        Institution institution = new Institution(1L, "marek", "masdas", "sadasd", "sadasd", "Dasdas", new ArrayList<>());
        InstitutionDto dto = institutionMapper.toDto(institution);
        when(institutionService.addInstitution(institution)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/institutions").content(objectMapper.writeValueAsString(institution)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("marek"))
                .andExpect(jsonPath("$.city").value("masdas"))
                .andExpect(jsonPath("$.doctors").value(new ArrayList<>()));
    }

    @Test
    void getInstitutions_ReturnListOfInstitutions() throws Exception {
        when(institutionService.getInstitutions(any()))
                .thenReturn(List.of(
                        new InstitutionDto(1L, "sadas", "Sdad", new ArrayList<>()),
                        new InstitutionDto(2L, "dsf", "sda", new ArrayList<>())));
        mockMvc.perform(MockMvcRequestBuilders.get("/institutions").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[0].name").value("sadas"))
                .andExpect(jsonPath("$[1].name").value("dsf"))
                .andExpect(jsonPath("$[0].city").value("Sdad"))
                .andExpect(jsonPath("$[1].city").value("sda"));
    }

    @Test
    void getById_InstitutionExist_ReturnInstitutionDto() throws Exception {
        Long id = 1L;
        when(institutionService.getById(id)).thenReturn(new InstitutionDto(id, "name", "City", new ArrayList<>()));

        mockMvc.perform(MockMvcRequestBuilders.get("/institutions/1").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.city").value("City"));
    }

    @Test
    void assignDoctor_DoctorAndInstitutionExist_ReturnInstitutionDto() throws Exception {
        when(institutionService.assignDoctor(any(), any())).thenReturn(new InstitutionDto(1L, "name", "City", new ArrayList<>()));

        mockMvc.perform(MockMvcRequestBuilders.patch("/institutions/1/doctors/1").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.city").value("City"));
    }
}