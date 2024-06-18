package com.Mafiuz04.medicalclinic.service;

import com.Mafiuz04.medicalclinic.exception.MedicalClinicException;
import com.Mafiuz04.medicalclinic.mapper.DoctorMapper;
import com.Mafiuz04.medicalclinic.model.Doctor;
import com.Mafiuz04.medicalclinic.model.DoctorCreateDto;
import com.Mafiuz04.medicalclinic.model.DoctorDto;
import com.Mafiuz04.medicalclinic.model.MedicalUser;
import com.Mafiuz04.medicalclinic.repository.JPADoctorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DoctorServiceTest {
    DoctorMapper doctorMapper;
    JPADoctorRepository doctorRepository;
    DoctorService doctorService;

    @BeforeEach
    void setup() {
        this.doctorMapper = Mappers.getMapper(DoctorMapper.class);
        this.doctorRepository = Mockito.mock(JPADoctorRepository.class);
        this.doctorService = new DoctorService(doctorMapper, doctorRepository);
    }

    @Test
    void addDoctor_DoctorAdded_DoctorDtoReturned() {
        //given
        DoctorCreateDto doctorCreateDto = new DoctorCreateDto("Cardio", new MedicalUser(1L, "adam", "marek", "mar@", "sadas"));
        Doctor doctor = doctorMapper.createToDoctor(doctorCreateDto);
        when(doctorRepository.save(any())).thenReturn(doctor);
        //when
        DoctorDto doctorDto = doctorService.addDoctor(doctorCreateDto);
        //then
        Assertions.assertEquals("Cardio", doctorDto.getSpecialization());
    }

    @Test
        /// wtf
    void getDoctors_DoctorsExist_ReturnListOfDoctors() {
        //given
        List<Doctor> doctors = new ArrayList<>();
        MedicalUser user = new MedicalUser(1L, "Adam", "Marczyk", "asd@", "password");
        doctors.add(new Doctor(1L, "Cardio", new ArrayList<>(), new ArrayList<>(), user));
        doctors.add(new Doctor(2L, "Endo", new ArrayList<>(), new ArrayList<>(), user));
        doctors.add(new Doctor(3L, "Gimno", new ArrayList<>(), new ArrayList<>(), user));
        Pageable pageable = PageRequest.of(0, 3);
        Page<Doctor> page = new PageImpl<>(doctors);
        when(doctorRepository.findAll(pageable)).thenReturn(page);
        //when
        List<DoctorDto> doctors1 = doctorService.getDoctors(pageable);
        //then
        Assertions.assertEquals(3, doctors1.size());
    }

    @Test
    void getDoctorById_DoctorExist_returnDoctorDto() {
        //given
        Doctor doctor = new Doctor(1L, "CArdio", new ArrayList<>(), new ArrayList<>(), new MedicalUser());
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        //when
        DoctorDto doctorDto = doctorService.getById(doctor.getId());
        //then
        Assertions.assertNotNull(doctorDto);
        Assertions.assertEquals(1, doctorDto.getId());
    }

    @Test
    void getDoctorById_DoctorDoesNotExist_ThrowException(){
        Long doctorId = 1L;

        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class, () -> doctorService.getById(1L));

        Assertions.assertEquals("Doctor with given id doesn't exist", exception.getMessage());
    }
}
