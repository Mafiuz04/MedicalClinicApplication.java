package com.Mafiuz04.medicalclinic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String specialization;
    @ManyToMany(mappedBy = "doctors")
    private List<Institution> institutions = new ArrayList<>();
    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments = new ArrayList<>();
}
