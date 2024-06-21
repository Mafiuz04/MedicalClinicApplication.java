package com.Mafiuz04.medicalclinic.controller;

import com.Mafiuz04.medicalclinic.model.PatientCreateDto;
import com.Mafiuz04.medicalclinic.model.PatientDto;
import com.Mafiuz04.medicalclinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/patients")
@RestController
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @GetMapping()
    public List<PatientDto> getPatients(Pageable pageable) {
        return patientService.getPatients(pageable);
    }

    @GetMapping("/{id}")
    public PatientDto getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDto addPatient(@RequestBody PatientCreateDto patient) {
        return patientService.addPatient(patient);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }

    @PutMapping("/{id}")
    public PatientDto updatePatient(@PathVariable Long id, @RequestBody PatientCreateDto updatedPatient) {
        return patientService.updatePatient(id, updatedPatient);
    }
}
