package com.Mafiuz04.medicalclinic.service;

import com.Mafiuz04.medicalclinic.mapper.InstitutionMapper;
import com.Mafiuz04.medicalclinic.model.Doctor;
import com.Mafiuz04.medicalclinic.model.Institution;
import com.Mafiuz04.medicalclinic.model.InstitutionDto;
import com.Mafiuz04.medicalclinic.model.MedicalUser;
import com.Mafiuz04.medicalclinic.repository.JPADoctorRepository;
import com.Mafiuz04.medicalclinic.repository.JPAInstitutionRepository;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InstitutionServiceTest {
    JPAInstitutionRepository institutionRepository;
    JPADoctorRepository doctorRepository;
    InstitutionMapper institutionMapper;
    InstitutionService institutionService;

    @BeforeEach
    void setup() {
        this.institutionRepository = Mockito.mock(JPAInstitutionRepository.class);
        this.doctorRepository = Mockito.mock(JPADoctorRepository.class);
        this.institutionMapper = Mappers.getMapper(InstitutionMapper.class);
        this.institutionService = new InstitutionService(institutionRepository, doctorRepository, institutionMapper);
    }

    @Test
    void addInstitution_InstitutionDoesNotExist_InstitutionDtoReturned() {
        //given
        Institution institution = createInstitution();
        when(institutionRepository.existsByName(institution.getName())).thenReturn(false);
        when(institutionRepository.save(institution)).thenReturn(institution);
        //when
        InstitutionDto institutionDto = institutionService.addInstitution(institution);
        //then
        Assertions.assertNotNull(institutionDto.getDoctors());
        verify(institutionRepository).save(institution);
    }

    @Test
    void getInstitutions_InstitutionsExist_ListOfInstitutionsDtoReturned() {
        //given
        List<Institution> institutions = new ArrayList<>();
        institutions.add(createInstitution());
        institutions.add(createInstitution());
        institutions.add(createInstitution());
        Pageable pageable = PageRequest.of(0, 10);
        Page<Institution> page = new PageImpl<>(institutions);
        when(institutionRepository.findAll(pageable)).thenReturn(page);
        //when
        List<InstitutionDto> institutions1 = institutionService.getInstitutions(pageable);
        //then
        Assertions.assertEquals(3, institutions1.size());
        Assertions.assertNotNull(institutions1);
    }

    @Test
    void getInstitutionById_InstitutionExist_ReturnInstitutionDto() {
        //given
        Institution institution = createInstitution();
        when(institutionRepository.findById(institution.getId())).thenReturn(Optional.of(institution));
        //when
        InstitutionDto institutionById = institutionService.getById(institution.getId());
        //then
        Assertions.assertEquals(institution.getId(), institutionById.getId());
        Assertions.assertInstanceOf(InstitutionDto.class, institutionById);
    }

    @Test
    void assignDoctor_CorrectData_InstitutionDtoReturned(){
        //given
        Doctor doctor = createDoctor();
        Institution institution = createInstitution();
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(institutionRepository.findById(doctor.getId())).thenReturn(Optional.of(institution));
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        when(institutionRepository.save(institution)).thenReturn(institution);
        //when
        InstitutionDto institutionDto = institutionService.assignDoctor(doctor.getId(), institution.getId());
        //then
        Assertions.assertEquals(1,institutionDto.getDoctors().size());

    }
    Institution createInstitution() {
        return new Institution(1L, "Marcówka", "Łódź", "92-12", "Moraks", "213", new ArrayList<>());
    }
    Doctor createDoctor(){
        MedicalUser user = new MedicalUser(1L,"Marek", "Adamczyk","sad@", "sadasd");
        return new Doctor(1L,"Cardio",new ArrayList<>(),new ArrayList<>(),user);
    }
}
