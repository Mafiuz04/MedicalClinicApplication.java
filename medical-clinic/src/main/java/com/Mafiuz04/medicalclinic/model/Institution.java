package com.Mafiuz04.medicalclinic.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Institution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String city;
    private String postCode;
    private String address;
    private String buildingNumber;
}
