package com.Mafiuz04.medicalclinic.controller;

import com.Mafiuz04.medicalclinic.mapper.UserMapper;
import com.Mafiuz04.medicalclinic.model.ChangePassword;
import com.Mafiuz04.medicalclinic.model.MedicalUser;
import com.Mafiuz04.medicalclinic.model.UserDto;
import com.Mafiuz04.medicalclinic.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void addUser_ReturnUserDto() throws Exception {
        MedicalUser user = new MedicalUser(1L, "Marek", "Anodek","asdaw@","sadasd");
        UserDto dto = userMapper.toDto(user);
        when(userService.createUser(user)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/users").content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Marek"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Anodek"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("asdaw@"));
    }

    @Test
    void updateUserPassword_ReturnUserDto() throws Exception {
        ChangePassword changePassword = new ChangePassword("dsadasd");
        Long id = 1L;
        MedicalUser user = new MedicalUser(id, "Marek", "Anodek","asdaw@","sadasd");
        UserDto dto = userMapper.toDto(user);
        when(userService.changeUserPassword(id,changePassword)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/1/password")
                        .content(objectMapper.writeValueAsString(changePassword.getPassword()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Marek"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Anodek"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("asdaw@"));
    }
}
