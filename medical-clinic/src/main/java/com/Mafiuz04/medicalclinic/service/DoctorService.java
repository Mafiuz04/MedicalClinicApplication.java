package com.Mafiuz04.medicalclinic.service;

import com.Mafiuz04.medicalclinic.exception.MedicalClinicException;
import com.Mafiuz04.medicalclinic.mapper.DoctorMapper;
import com.Mafiuz04.medicalclinic.model.*;
import com.Mafiuz04.medicalclinic.repository.JPADoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorMapper doctorMapper;
    private final JPADoctorRepository doctorRepository;

    public DoctorDto addDoctor(DoctorCreateDto createdDoctor) {
        Doctor doctor = doctorMapper.createToDoctor(createdDoctor);
        doctorRepository.save(doctor);
        return doctorMapper.tooDto(doctor);
    }

    public List<DoctorDto> getDoctors(Pageable pageable) {
        List<Doctor> allList = doctorRepository.findAll(pageable).getContent();
        return doctorMapper.listToDto(allList);
    }

    public DoctorDto getById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new MedicalClinicException("Doctor with given id doesn't exist", HttpStatus.BAD_REQUEST));
        return doctorMapper.tooDto(doctor);
    }
}
