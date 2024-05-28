package com.Mafiuz04.medicalclinic.model;

import lombok.Data;

import java.util.List;

@Data
public class DoctorDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String specialization;
    private List<SimpleInstitutionDto> institutions;
    private List<AppointmentDto> appointments;
}
