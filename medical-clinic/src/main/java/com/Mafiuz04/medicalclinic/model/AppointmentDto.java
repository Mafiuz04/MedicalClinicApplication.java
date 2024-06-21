package com.Mafiuz04.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@AllArgsConstructor
@Data
public class AppointmentDto {
    private Long id;
    private Long doctorId;
    private Long patientId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
