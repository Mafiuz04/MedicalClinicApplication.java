package com.Mafiuz04.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class PatientDto {
    private Long id;
    private LocalDate birthday;
    private List<AppointmentDto> appointments;
    private MedicalUser medicalUser;
}
