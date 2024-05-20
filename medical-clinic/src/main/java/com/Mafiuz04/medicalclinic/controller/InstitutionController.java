package com.Mafiuz04.medicalclinic.controller;

import com.Mafiuz04.medicalclinic.model.Institution;
import com.Mafiuz04.medicalclinic.service.InstitutionService;
import lombok.RequiredArgsConstructor;
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
    public Institution addInstitution(@RequestBody Institution institution) {
        return institutionService.addInstitution(institution);
    }
    @GetMapping
    public List<Institution> getInstitutions(){
        return institutionService.getInstitutions();
    }
    @GetMapping("/{id}")
    public  Institution getById(@PathVariable Long id){
        return institutionService.getById(id);
    }
}
