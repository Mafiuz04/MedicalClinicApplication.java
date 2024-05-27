package com.Mafiuz04.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleInstitutionDto {
    private Long id;
    private String name;
    private String city;
}
