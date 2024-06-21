package com.Mafiuz04.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class InstitutionDto {
    private Long id;
    private String name;
    private String city;
    private List<SimpleDoctorDto> doctors;
}
