package com.Mafiuz04.medicalclinic.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientDto {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthday;
}
