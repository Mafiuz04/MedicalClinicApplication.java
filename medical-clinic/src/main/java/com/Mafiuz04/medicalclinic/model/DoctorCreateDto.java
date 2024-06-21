package com.Mafiuz04.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoctorCreateDto {
    private String specialization;
    private MedicalUser medicalUser;
}
