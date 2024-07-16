package com.Mafiuz04.medicalclinic.controller;

import com.Mafiuz04.medicalclinic.model.Institution;
import com.Mafiuz04.medicalclinic.model.InstitutionDto;
import com.Mafiuz04.medicalclinic.service.InstitutionService;
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

@RequestMapping("/institutions")
@RestController
@RequiredArgsConstructor
public class InstitutionController {

    private final InstitutionService institutionService;

    @Operation(summary = "Add institution to app.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Institution added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InstitutionDto.class))}),
            @ApiResponse(responseCode = "400", description = "Given Institution already exist in system.",
                    content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InstitutionDto addInstitution(@RequestBody Institution institution) {
        return institutionService.addInstitution(institution);
    }

    @Operation(summary = "Get all institutions existing in database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All available institutions listed below",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InstitutionDto.class))})
    })
    @GetMapping
    public List<InstitutionDto> getInstitutions(Pageable pageable) {
        return institutionService.getInstitutions(pageable);
    }

    @Operation(summary = "Get a institution by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Institution found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InstitutionDto.class))}),
            @ApiResponse(responseCode = "400", description = "There is no institution with given ID.",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public InstitutionDto getById(@PathVariable Long id) {
        return institutionService.getById(id);
    }

    @Operation(summary = "Assign by id, doctor to institution.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor assign.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InstitutionDto.class))}),
            @ApiResponse(responseCode = "400", description = "There is no Doctor with given ID.",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "There is no Institution with given ID.",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Doctor already assign.",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Institution already assign.",
                    content = @Content)
    })
    @PatchMapping("/{institutionId}/doctors/{doctorId}")
    public InstitutionDto assignDoctor(@PathVariable Long institutionId, @PathVariable Long doctorId) {
        return institutionService.assignDoctor(doctorId, institutionId);
    }
}
