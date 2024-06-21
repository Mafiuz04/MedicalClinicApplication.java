package com.Mafiuz04.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DoctorDto {
    private Long id;
    private MedicalUser medicalUser;
    private String specialization;
    private List<SimpleInstitutionDto> institutions;
    private List<AppointmentDto> appointments;
}
