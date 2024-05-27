package com.Mafiuz04.medicalclinic.service;

import com.Mafiuz04.medicalclinic.exception.MedicalClinicException;
import com.Mafiuz04.medicalclinic.mapper.InstitutionMapper;
import com.Mafiuz04.medicalclinic.model.*;
import com.Mafiuz04.medicalclinic.repository.JPADoctorRepository;
import com.Mafiuz04.medicalclinic.repository.JPAInstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstitutionService {

    private final JPAInstitutionRepository institutionRepository;
    private final JPADoctorRepository doctorRepository;
    private final InstitutionMapper institutionMapper;

    public InstitutionDto addInstitution(Institution institution) {
        if (institutionRepository.existsByName(institution.getName())){
            throw new MedicalClinicException("Given Institution already exist in system.", HttpStatus.BAD_REQUEST);
        }
        return institutionMapper.mapToDto(institutionRepository.save(institution));
    }

    public List<InstitutionDto> getInstitutions() {
        return institutionMapper.mapListToDto( institutionRepository.findAll());
    }

    public InstitutionDto getById(Long id) {
        Institution institution = institutionRepository.findById(id)
                .orElseThrow(() -> new MedicalClinicException("There is no institution with given ID", HttpStatus.BAD_REQUEST));
        return institutionMapper.mapToDto(institution);
    }
    public InstitutionDto assignDoctor(Long doctorId, Long institutionId){
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new MedicalClinicException("There is no Doctor with given ID", HttpStatus.BAD_REQUEST));
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new MedicalClinicException("There is no Institution with given ID", HttpStatus.BAD_REQUEST));
        List<Doctor> doctors = institution.getDoctors();
        if(doctors.contains(doctor)){
            throw new MedicalClinicException("Doctor already assign", HttpStatus.BAD_REQUEST);
        }
        List<Institution> institutions = doctor.getInstitutions();
        if(institutions.contains(institution)){
            throw new MedicalClinicException("Institution already assign", HttpStatus.BAD_REQUEST);
        }
        institutions.add(institution);
        doctors.add(doctor);
        doctorRepository.save(doctor);
        institutionRepository.save(institution);
        return institutionMapper.mapToDto(institution);
    }

}
