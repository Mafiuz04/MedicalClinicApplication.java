package com.Mafiuz04.medicalclinic.service;

import com.Mafiuz04.medicalclinic.exception.MedicalClinicException;
import com.Mafiuz04.medicalclinic.mapper.DoctorMapper;
import com.Mafiuz04.medicalclinic.model.*;
import com.Mafiuz04.medicalclinic.repository.JPADoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorMapper doctorMapper;
    private final JPADoctorRepository doctorRepository;

    public DoctorDto addDoctor(Doctor doctor) {
        if (doctorRepository.exists(Example.of(doctor))) {
            throw new MedicalClinicException("Given Doctor already exist in system.", HttpStatus.BAD_REQUEST);
        }
        doctorRepository.save(doctor);
        return doctorMapper.mapToDto(doctor);
    }

    public List<DoctorDto> getDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctorMapper.mapListToDto(doctors);
    }

    public DoctorDto getById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new MedicalClinicException("Doctor with given id doesn't exist", HttpStatus.BAD_REQUEST));
        return doctorMapper.mapToDto(doctor);
    }
}
