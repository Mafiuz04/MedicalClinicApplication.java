package com.Mafiuz04.medicalclinic.service;

import com.Mafiuz04.medicalclinic.exception.MedicalClinicException;
import com.Mafiuz04.medicalclinic.mapper.PatientMapper;
import com.Mafiuz04.medicalclinic.model.MedicalUser;
import com.Mafiuz04.medicalclinic.model.Patient;
import com.Mafiuz04.medicalclinic.model.PatientCreateDto;
import com.Mafiuz04.medicalclinic.model.PatientDto;
import com.Mafiuz04.medicalclinic.repository.JPAPatientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import java.util.stream.Stream;

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
        Assertions.assertEquals("asd@", patient1.getMedicalUser().getEmail());
    }

    @Test
    void getPatientById_PatientDoesNotExist_ThrowsException() {
        //given
        Long id = 1L;
        //when
        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class, () -> patientService.getPatientById(id));
        //then
        Assertions.assertEquals("There is no patient with given email.", exception.getMessage());
    }

    @Test
    void addPatient_CorrectData_PatientDtoReturned() {
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
    void addPatient_AnyDataNull_ThrowException() {
        //given
        PatientCreateDto patientCreateDto = new PatientCreateDto("sadasdas", "asdasd", LocalDate.of(2000, 12, 20), null);
        //when
        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class, () -> patientService.addPatient(patientCreateDto));
        //then
        Assertions.assertEquals("Please make sure that all data are included", exception.getMessage());
    }

    @Test
    void addPatient_PatientExist_ThrowException() {
        //given
        MedicalUser user1 = new MedicalUser(1L, "Marek", "Marczyk", "asdasd@", "sadasd");
        PatientCreateDto patientCreateDto = new PatientCreateDto("sadasdas", "asdasd", LocalDate.of(2000, 12, 20), user1);
        Patient patient = patientMapper.createToPatient(patientCreateDto);
        List<Patient> patients = new ArrayList<>();
        patients.add(patient);
        when(patientRepository.findAll()).thenReturn(patients);
        //when
        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class, () -> patientService.addPatient(patientCreateDto));
        //then
        Assertions.assertEquals("The patient with the provided e-mail address already exists in our system", exception.getMessage());
    }

    @Test
    void updatePatient_PatientExist_PatientDtoReturned() {
        //given
        Patient patient = createPatient(1L, "1234");
        PatientCreateDto updatedPatient = patientMapper.patientToCreate(createPatient(1L, "1234"));
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        //when
        PatientDto patientDto = patientService.updatePatient(patient.getId(), updatedPatient);
        //then
        Assertions.assertEquals("asd@", patientDto.getMedicalUser().getEmail());
        Assertions.assertNotNull(patientDto.getMedicalUser());
    }

    @Test
    void updatePatient_PatientDoesNotExist_PatientDtoReturned() {
        //given
        Long id = 1L;
        PatientCreateDto patientCreateDto = new PatientCreateDto("sadas", "Sdasd", LocalDate.of(2000, 12, 12), new MedicalUser());
        //when
        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class, () -> patientService.updatePatient(id, patientCreateDto));
        //then
        Assertions.assertEquals("We can not update patient, wrong id.", exception.getMessage());
    }

    @Test
    void updatePatient_PatientWrongMail_PatientDtoReturned() {
        Long id = 1L;
        MedicalUser user = new MedicalUser(1L,"sdas","Asdasd","asdasd","sdasd");
        MedicalUser user1 = new MedicalUser(1L,"sdas","Asdasd","assddasd","sdasd");
        PatientCreateDto patientCreateDto = new PatientCreateDto("sadas", "Sdasd", LocalDate.of(2000, 12, 12), user);
        Patient patient = new Patient(1L,"ASdasd0","sadasdasd",LocalDate.of(2000, 12, 12), new ArrayList<>(), user1);
        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));
        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class, () -> patientService.updatePatient(id, patientCreateDto));
        Assertions.assertEquals("The patient with the provided e-mail address does not exists in our system",exception.getMessage());
    }

    @Test
    void updatePatient_MissingData_PatientDtoReturned(){
        Long id = 1L;
        MedicalUser user = new MedicalUser(1L,"sdas","Asdasd","asdasd","sdasd");
        PatientCreateDto patientCreateDto = new PatientCreateDto("sadas", null, LocalDate.of(2000, 12, 12), user);
        Patient patient = new Patient(1L,"ASdasd0","sadasdasd",LocalDate.of(2000, 12, 12), new ArrayList<>(), user);
        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));
        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class, () -> patientService.updatePatient(id, patientCreateDto));
        Assertions.assertEquals("Please make sure that all data are included",exception.getMessage());
    }

    @Test
    void updatePatient_IdCardNumberChanged_PatientDtoReturned(){
        Long id = 1L;
        MedicalUser user = new MedicalUser(1L,"sdas","Asdasd","asdasd","sdasd");
        PatientCreateDto patientCreateDto = new PatientCreateDto("sadas", "Asda", LocalDate.of(2000, 12, 12), user);
        Patient patient = new Patient(1L,"ASdasd0","sadasdasd",LocalDate.of(2000, 12, 12), new ArrayList<>(), user);
        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));
        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class, () -> patientService.updatePatient(id, patientCreateDto));
        Assertions.assertEquals("You can not change ID card number.",exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("providePatientsWithNull")
    void checkPatientData_ReturnPatientWithNullField(PatientCreateDto patientCreateDto){
        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class, () -> patientService.checkData(patientCreateDto));
        Assertions.assertEquals("Please make sure that all data are included",exception.getMessage());
    }

    private static Stream<Arguments> providePatientsWithNull(){
        return Stream.of(
                Arguments.of(new PatientCreateDto(null,"sada",LocalDate.of(1999,12,12),new MedicalUser(1L,"asdasd","sadasd","asdasd","sda"))),
                Arguments.of(new PatientCreateDto("sadas",null,LocalDate.of(1999,12,12),new MedicalUser(1L,"asdasd","sadasd","asdasd","sda"))),
                Arguments.of(new PatientCreateDto("sada","sada",null,new MedicalUser(1L,"asdasd","sadasd","asdasd","sda"))),
                Arguments.of(new PatientCreateDto("sada","sada",LocalDate.of(1999,12,12),null)),
                Arguments.of(new PatientCreateDto("sada","sada",LocalDate.of(1999,12,12),new MedicalUser(1L,null,"sadasd","asdasd","sda"))),
                Arguments.of(new PatientCreateDto("sada","sada",LocalDate.of(1999,12,12),new MedicalUser(1L,"asdasd",null,"asdasd","sda"))),
                Arguments.of(new PatientCreateDto("sada","sada",LocalDate.of(1999,12,12),new MedicalUser(1L,"asdasd","sadasd",null,"sda"))),
                Arguments.of(new PatientCreateDto("sada","sada",LocalDate.of(1999,12,12),new MedicalUser(1L,"asdasd","sadasd","asdasd",null)))
        );
    }

    Patient createPatient(Long id, String idCardNumber) {
        MedicalUser user = new MedicalUser(1L, "Adam", "Marczyk", "asd@", "password");
        return new Patient(id, idCardNumber, "sad223", LocalDate.of(2000, 12, 2), new ArrayList<>(), user);
    }
}

