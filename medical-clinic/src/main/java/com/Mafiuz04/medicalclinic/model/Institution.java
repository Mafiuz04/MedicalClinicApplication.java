package com.Mafiuz04.medicalclinic.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Institution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name",unique = true)
    private String name;
    private String city;
    private String postCode;
    private String address;
    private String buildingNumber;
    @ManyToMany
    private List<Doctor> doctors = new ArrayList<>();
}
