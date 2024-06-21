package com.Mafiuz04.medicalclinic.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Institution institution)) return false;
        return id != null && id.equals(institution.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
