package com.Mafiuz04.medicalclinic.service;

import com.Mafiuz04.medicalclinic.mapper.PatientMapper;
import com.Mafiuz04.medicalclinic.model.MedicalUser;
import com.Mafiuz04.medicalclinic.model.Patient;
import com.Mafiuz04.medicalclinic.model.PatientCreateDto;
import com.Mafiuz04.medicalclinic.model.PatientDto;
import com.Mafiuz04.medicalclinic.repository.JPAPatientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PatientServiceTest {
    PatientService patientService;
    PatientMapper patientMapper;
    JPAPatientRepository patientRepository;

    @BeforeEach
    void setup() {
        this.patientMapper = Mappers.getMapper(PatientMapper.class);
        this.patientRepository = Mockito.mock(JPAPatientRepository.class);
        this.patientService = new PatientService(patientMapper, patientRepository);
    }

    @Test
    void getPatients_PatientsExists_PatientsReturned() {
        //given
        List<Patient> patients = new ArrayList<>();
        patients.add(createPatient(1L, "321"));
        patients.add(createPatient(2L, "123"));
        Pageable pageable = PageRequest.of(0, 10);
        Page<Patient> patientPage = new PageImpl<>(patients);
        when(patientRepository.findAll(pageable)).thenReturn(patientPage);
        //when
        List<PatientDto> result = patientService.getPatients(pageable);
        //then
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void getPatientById_PatientExist_PatientReturned() {
        //given
        Patient patient = createPatient(1L, "123");
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        //when
        PatientDto patient1 = patientService.getPatientById(patient.getId());
        //then
        Assertions.assertEquals(1, patient1.getId());
    }

    @Test
    void addPatient_PatientDtoReturned() {
        //given
        MedicalUser user = new MedicalUser(1L, "Marek", "Marek", "123@", "sadas");
        PatientCreateDto patientCreateDto = new PatientCreateDto("12312", "213123123", LocalDate.of(1998, 11, 12), user);
        Patient patient = patientMapper.createToPatient(patientCreateDto);
        when(patientRepository.save(any())).thenReturn(patient);
        //when
        PatientDto patientDto = patientService.addPatient(patientCreateDto);
        //then
        Assertions.assertEquals(1L, patientDto.getMedicalUser().getId());
        Assertions.assertEquals("123@", patientDto.getMedicalUser().getEmail());

    }

    @Test
    void updatePatient_PatientExist_PatientDtoReturned() {
        //given
        Patient patient = createPatient(1L, "1234");
        Patient updatedPatient = createPatient(1L, "1234");
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        //when
        PatientDto patientDto = patientService.updatePatient(patient.getId(), updatedPatient);
        //then
        Assertions.assertEquals("asd@", patientDto.getMedicalUser().getEmail());
        Assertions.assertNotNull(patientDto.getMedicalUser());


    }

    Patient createPatient(Long id, String idCardNumber) {
        MedicalUser user = new MedicalUser(1L,"Adam","Marczyk","asd@","password");
        return new Patient(id, idCardNumber, "sad223", LocalDate.of(2000, 12, 2), new ArrayList<>(), user);
    }
}

