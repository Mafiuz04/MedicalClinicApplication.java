package com.Mafiuz04.medicalclinic.controller;

import com.Mafiuz04.medicalclinic.model.DoctorCreateDto;
import com.Mafiuz04.medicalclinic.model.DoctorDto;
import com.Mafiuz04.medicalclinic.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/doctors")
@RestController
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorDto addDoctor(@RequestBody DoctorCreateDto doctor){
       return doctorService.addDoctor(doctor);
    }

    @GetMapping
    public List<DoctorDto> getDoctors(Pageable pageable) {
      return doctorService.getDoctors(pageable);
    }

    @GetMapping("/{id}")
    public DoctorDto getDoctorById(@PathVariable Long id) {
        return doctorService.getById(id);
    }

}
