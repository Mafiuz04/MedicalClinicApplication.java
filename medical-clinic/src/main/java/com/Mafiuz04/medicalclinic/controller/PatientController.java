package com.Mafiuz04.medicalclinic.controller;

import com.Mafiuz04.medicalclinic.model.PatientCreateDto;
import com.Mafiuz04.medicalclinic.model.PatientDto;
import com.Mafiuz04.medicalclinic.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all patients existing in database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All available patients listed below",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class))})
    })
    @GetMapping()
    public List<PatientDto> getPatients(Pageable pageable) {
        return patientService.getPatients(pageable);
    }

    @Operation(summary = "Get a patient by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class))}),
            @ApiResponse(responseCode = "400", description = "There is no patient with given id",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public PatientDto getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id);
    }

    @Operation(summary = "Add patient to app.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class))}),
            @ApiResponse(responseCode = "400", description = "Please make sure that all data are included",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "The patient with the provided e-mail address already exists in our system",
                    content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDto addPatient(@RequestBody PatientCreateDto patient) {
        return patientService.addPatient(patient);
    }

    @Operation(summary = "Delete patient by id.")
    @ApiResponse(responseCode = "204")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }

    @Operation(summary = "Update patient data by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient data updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class))}),
            @ApiResponse(responseCode = "400", description = "We can not update patient, wrong id.",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "The patient with the provided e-mail address does not exists in our system",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Please make sure that all data are included",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "You can not change ID card number.",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "You can not change ID card number.",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public PatientDto updatePatient(@PathVariable Long id, @RequestBody PatientCreateDto updatedPatient) {
        return patientService.updatePatient(id, updatedPatient);
    }
}
