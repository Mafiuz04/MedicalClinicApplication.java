package com.Mafiuz04.medicalclinic.service;

import com.Mafiuz04.medicalclinic.exception.MedicalClinicException;
import com.Mafiuz04.medicalclinic.mapper.InstitutionMapper;
import com.Mafiuz04.medicalclinic.model.*;
import com.Mafiuz04.medicalclinic.repository.JPADoctorRepository;
import com.Mafiuz04.medicalclinic.repository.JPAInstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstitutionService {

    private final JPAInstitutionRepository institutionRepository;
    private final JPADoctorRepository doctorRepository;
    private final InstitutionMapper institutionMapper;

    //TC1: W przypadku gdy instytucja o danej nazwie nie istnieje, zostanie zapisana w repozytorium( wykona się metoda institutionRepository.save();)
    //TC2: W przypadku gdy nazwa podaje instytucji już istnieje, zostanie rzucony wyjątek.
    @Transactional
    public InstitutionDto addInstitution(Institution institution) {
        if (institutionRepository.existsByName(institution.getName())) {
            throw new MedicalClinicException("Given Institution already exist in system.", HttpStatus.BAD_REQUEST);
        }
        return institutionMapper.toDto(institutionRepository.save(institution));
    }

    //TC1: W przypadku istnienia listy instytucji, zostaje ona zwrócona.
    public List<InstitutionDto> getInstitutions(Pageable pageable) {
        List<Institution> institutions = institutionRepository.findAll(pageable).getContent();
        return institutionMapper.listToDto(institutions);
    }

    //TC1: W przypadku gdy instytucja o danym ID istnieje, zostanie przemapowana i zwrcona.
    //TC2: W przypadku gdy instytucja o danym ID nie istnieje, zostanie rzucony wyjątek.
    public InstitutionDto getById(Long id) {
        Institution institution = institutionRepository.findById(id)
                .orElseThrow(() -> new MedicalClinicException("There is no institution with given ID", HttpStatus.BAD_REQUEST));
        return institutionMapper.toDto(institution);
    }

    //TC1: W przypadku gdy doktor oraz instytucja o podanych ID istnieją, doktor nie jest przypisany do instytucji i instytucja ni jest przypisana do doctora,
    //zostjają przypisane i zapisane w repozytorium.
    //TC2: W przypadku gdy doctor o podanym ID nie istnieje,ostanie zwrócony Optional<empty>, zostanie rzucony wyjątek.
    //TC3: W przypadku gdy doctor o podanym ID istnieje, a instytucja o podanym ID nie istnieje, zostanie zwrócony Optional<empty>, zostanie rzucony wyjatek.
    //TC4: W przypadku gdy doktor oraz instytucja o podanych ID istnieją, instytucja ma już przypisanego danego doktora, powinien zostać rzucony wyjatek.
    //TC5:W przypadku gdy doktor oraz instytucja o podanych ID istnieją, instytucja nie ma przypisanego danego doktora,
// ale doctor ma przypisaną instytucję, powinien zostać rzucony wyjątek.
    @Transactional
    public InstitutionDto assignDoctor(Long doctorId, Long institutionId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new MedicalClinicException("There is no Doctor with given ID", HttpStatus.BAD_REQUEST));
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new MedicalClinicException("There is no Institution with given ID", HttpStatus.BAD_REQUEST));
        List<Doctor> doctors = institution.getDoctors();
        if (doctors.contains(doctor)) {
            throw new MedicalClinicException("Doctor already assign", HttpStatus.BAD_REQUEST);
        }
        List<Institution> institutions = doctor.getInstitutions();
        if (institutions.contains(institution)) {
            throw new MedicalClinicException("Institution already assign", HttpStatus.BAD_REQUEST);
        }
        institutions.add(institution);
        doctors.add(doctor);
        doctorRepository.save(doctor);
        institutionRepository.save(institution);
        return institutionMapper.toDto(institution);
    }
}
