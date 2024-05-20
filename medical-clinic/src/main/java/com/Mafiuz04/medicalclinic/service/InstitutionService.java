package com.Mafiuz04.medicalclinic.service;

import com.Mafiuz04.medicalclinic.exception.MedicalClinicException;
import com.Mafiuz04.medicalclinic.model.Institution;
import com.Mafiuz04.medicalclinic.repository.JPAInstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstitutionService {

    private final JPAInstitutionRepository institutionRepository;

    public Institution addInstitution(Institution institution) {
        if (institutionRepository.exists(Example.of(institution))){
            throw new MedicalClinicException("Given Doctor already exist in system.", HttpStatus.BAD_REQUEST);
        }
        return institutionRepository.save(institution);
    }

    public List<Institution> getInstitutions() {
        return institutionRepository.findAll();
    }

    public Institution getById(Long id) {
        return institutionRepository.findById(id)
                .orElseThrow(() -> new MedicalClinicException("There is no institution with given ID", HttpStatus.BAD_REQUEST));
    }

}
