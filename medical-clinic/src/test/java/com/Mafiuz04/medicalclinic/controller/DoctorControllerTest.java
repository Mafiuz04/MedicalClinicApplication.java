package com.Mafiuz04.medicalclinic.controller;

import com.Mafiuz04.medicalclinic.mapper.DoctorMapper;
import com.Mafiuz04.medicalclinic.model.Doctor;
import com.Mafiuz04.medicalclinic.model.DoctorCreateDto;
import com.Mafiuz04.medicalclinic.model.DoctorDto;
import com.Mafiuz04.medicalclinic.model.MedicalUser;
import com.Mafiuz04.medicalclinic.service.DoctorService;
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
public class DoctorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    DoctorMapper doctorMapper;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    DoctorService doctorService;

    @Test
    void addDoctor_ReturnDoctorDto() throws Exception {
        MedicalUser user = new MedicalUser(1L, "Sdad", "dsad", "sadasd", "sad");
        DoctorCreateDto createDto = new DoctorCreateDto("Cardio", user);
        Doctor doctor = doctorMapper.createToDoctor(createDto);
        doctor.setId(1L);
        DoctorDto dto = doctorMapper.tooDto(doctor);
        when(doctorService.addDoctor(any())).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/doctors").content(objectMapper.writeValueAsString(createDto)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.medicalUser.id").value(1))
                .andExpect(jsonPath("$.specialization").value("Cardio"))
                .andExpect(jsonPath("$.institutions").value(new ArrayList<>()));
    }

    @Test
    void getDoctors_DoctorsExists_ReturnListOfDoctors() throws Exception {
        when(doctorService.getDoctors(any())).thenReturn(List.of(
                new DoctorDto(1L, new MedicalUser(1L, "sa", "sa", "Sa", "sa"), "cardio", new ArrayList<>(), new ArrayList<>()),
                new DoctorDto(2L, new MedicalUser(2L, "saa", "saa", "Saa", "saa"), "cardio", new ArrayList<>(), new ArrayList<>())
        ));

        mockMvc.perform(MockMvcRequestBuilders.get("/doctors").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[0].medicalUser.id").value(1))
                .andExpect(jsonPath("$[1].medicalUser.id").value(2));
    }

    @Test
    void getDoctorById_DoctorExist_ReturnDoctorDto() throws Exception {
        Long id = 1L;
        when(doctorService.getById(id)).thenReturn(new DoctorDto(1L, new MedicalUser(1L, "sa", "sa", "Sa", "sa"), "cardio", new ArrayList<>(), new ArrayList<>()));

        mockMvc.perform(MockMvcRequestBuilders.get("/doctors/1").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.medicalUser.id").value(1))
                .andExpect(jsonPath("$.specialization").value("cardio"))
                .andExpect(jsonPath("$.medicalUser.firstName").value("sa"));
    }
}
