package com.Mafiuz04.medicalclinic.controller;

import com.Mafiuz04.medicalclinic.model.DoctorCreateDto;
import com.Mafiuz04.medicalclinic.model.DoctorDto;
import com.Mafiuz04.medicalclinic.service.DoctorService;
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

@RequestMapping("/doctors")
@RestController
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @Operation(summary = "Add doctor to app.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Doctor added.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDto.class))})
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorDto addDoctor(@RequestBody DoctorCreateDto doctor){
       return doctorService.addDoctor(doctor);
    }

    @Operation(summary = "Get all doctors existing in database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All available doctors listed below.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDto.class))})
    })
    @GetMapping
    public List<DoctorDto> getDoctors(Pageable pageable) {
      return doctorService.getDoctors(pageable);
    }

    @Operation(summary = "Get a doctor by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDto.class))}),
            @ApiResponse(responseCode = "400", description = "Doctor with given id doesn't exist.",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public DoctorDto getDoctorById(@PathVariable Long id) {
        return doctorService.getById(id);
    }

}
