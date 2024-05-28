package com.Mafiuz04.medicalclinic.controller;

import com.Mafiuz04.medicalclinic.model.Institution;
import com.Mafiuz04.medicalclinic.model.InstitutionDto;
import com.Mafiuz04.medicalclinic.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/institutions")
@RestController
@RequiredArgsConstructor
public class InstitutionController {
    private final InstitutionService institutionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InstitutionDto addInstitution(@RequestBody Institution institution) {
        return institutionService.addInstitution(institution);
    }

    @GetMapping
    public List<InstitutionDto> getInstitutions(Pageable pageable) {
        return institutionService.getInstitutions(pageable);
    }

    @GetMapping("/{id}")
    public InstitutionDto getById(@PathVariable Long id) {
        return institutionService.getById(id);
    }

    @PatchMapping("/{institutionId}/doctors/{doctorId}")
    public InstitutionDto assignDoctor(@PathVariable Long institutionId, @PathVariable Long doctorId) {
        return institutionService.assignDoctor(doctorId, institutionId);
    }
}
