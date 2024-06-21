package com.Mafiuz04.medicalclinic.service;

import com.Mafiuz04.medicalclinic.exception.MedicalClinicException;
import com.Mafiuz04.medicalclinic.mapper.DoctorMapper;
import com.Mafiuz04.medicalclinic.model.*;
import com.Mafiuz04.medicalclinic.repository.JPADoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorMapper doctorMapper;
    private final JPADoctorRepository doctorRepository;

    @Transactional
    public DoctorDto addDoctor(DoctorCreateDto createdDoctor) {
        Doctor doctor = doctorMapper.createToDoctor(createdDoctor);
        doctorRepository.save(doctor);
        return doctorMapper.tooDto(doctor);
    }

    //TC1: W przypadku gdy istnieje lista doctorów, zostanie ona zwrócona.
    public List<DoctorDto> getDoctors(Pageable pageable) {
        List<Doctor> doctors = doctorRepository.findAll(pageable).getContent();
        return doctorMapper.listToDto(doctors);
    }

    //TC1: W przypadku gdy doctor o danym ID istnieje, zostanie przemapowany i zwrócony.
    //TC2: W przypadku gdy doctor o danym ID nie istnieje, zostanie rzucony wyjątek.
    public DoctorDto getById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new MedicalClinicException("Doctor with given id doesn't exist", HttpStatus.BAD_REQUEST));
        return doctorMapper.tooDto(doctor);
    }
}
