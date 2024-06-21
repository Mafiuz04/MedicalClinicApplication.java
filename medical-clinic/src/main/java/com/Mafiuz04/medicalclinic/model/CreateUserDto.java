package com.Mafiuz04.medicalclinic.model;

import lombok.Data;

@Data
public class CreateUserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
