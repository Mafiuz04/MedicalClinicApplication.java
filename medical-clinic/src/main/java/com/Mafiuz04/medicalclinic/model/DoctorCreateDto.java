package com.Mafiuz04.medicalclinic.model;

import lombok.Data;

@Data
public class DoctorCreateDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String specialization;
}
