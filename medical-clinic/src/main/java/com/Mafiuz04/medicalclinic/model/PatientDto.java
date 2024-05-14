package com.Mafiuz04.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PatientDto {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthday;
}
