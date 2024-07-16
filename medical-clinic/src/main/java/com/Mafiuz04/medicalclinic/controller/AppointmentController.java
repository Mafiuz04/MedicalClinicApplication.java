package com.Mafiuz04.medicalclinic.controller;

import com.Mafiuz04.medicalclinic.model.AppointmentDto;
import com.Mafiuz04.medicalclinic.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/appointments")
@RestController
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Operation(summary = "Get all patient's visits")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All assigned visits",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDto.class))})
    })
    @GetMapping("/patient/{patientId}")
    public List<AppointmentDto> getPatientAppointments(@PathVariable("patientId") Long patientId) {
        return appointmentService.getPatientAppointments(patientId);
    }

    @Operation(summary = "Get all doctor's available appointments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All available visits",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDto.class))})
    })
    @GetMapping("/doctor/{doctorId}")
    public List<AppointmentDto> getDoctorAvailableAppointments(@PathVariable("doctorId") Long doctorId) {
        return appointmentService.getDoctorAvailableAppointments(doctorId);
    }

    @Operation(summary = "Get all appointments by specialization and date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All available visits",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDto.class))})
    })
    @GetMapping("/doctor/specialization/{specialization}")
    public List<AppointmentDto> getAvailableAppointmentsBySpecialization(@PathVariable("specialization") String specialization,
                                                                         @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return appointmentService.getAvailableAppointmentsBySpecialization(specialization, date);
    }

    @Operation(summary = "Add appointment to app.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Appointment added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDto.class))}),
            @ApiResponse(responseCode = "400", description = "Appointment is no available any more, please check date.",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Please change hour, the doctor sees patient every 15 minutes.",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Time range already taken.",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Chosen Doctor does not exist.",
                    content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentDto addAppointment(@RequestBody AppointmentDto appointment) {
        return appointmentService.createAppointment(appointment);
    }

    @Operation(summary = "Assign patient to appointment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient assigned to appointment.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDto.class))}),
            @ApiResponse(responseCode = "400", description = "Chosen appointment does not exist.",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Appointment is no available any more, please check date..",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Chosen Patient does not exist.",
                    content = @Content)
    })
    @PatchMapping("/{appointmentId}/patients/{patientId}")
    public AppointmentDto assignPatient(@PathVariable Long appointmentId, @PathVariable Long patientId) {
        return appointmentService.assignPatient(appointmentId, patientId);
    }

    @Operation(summary = "Get all appointments existing in database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All  appointments listed below",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDto.class))})
    })
    @GetMapping
    public List<AppointmentDto> getAppointments(Pageable pageable) {
        return appointmentService.getAppointments(pageable);
    }
}
