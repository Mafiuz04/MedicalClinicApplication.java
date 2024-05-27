package com.Mafiuz04.medicalclinic.controller;

import com.Mafiuz04.medicalclinic.model.AppointmentDto;
import com.Mafiuz04.medicalclinic.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/appointments")
@RestController
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentDto addAppointment(@RequestBody AppointmentDto appointment) {
        return appointmentService.createAppointment(appointment);
    }

    @PatchMapping("/{appointmentId}/patients/{patientId}")
    @ResponseStatus(HttpStatus.OK)
    public AppointmentDto assignPatient(@PathVariable Long appointmentId, @PathVariable Long patientId) {
        return appointmentService.assignPatient(appointmentId, patientId);
    }

    @GetMapping
    public List<AppointmentDto> getAppointments() {
        return appointmentService.getAppointments();
    }
}
