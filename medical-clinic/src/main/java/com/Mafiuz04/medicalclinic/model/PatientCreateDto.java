package com.Mafiuz04.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PatientCreateDto {
    private String idCardNo;
    private String phoneNumber;
    private LocalDate birthday;
    private MedicalUser medicalUser;
}
