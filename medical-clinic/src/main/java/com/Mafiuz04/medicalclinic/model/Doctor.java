package com.Mafiuz04.medicalclinic.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor doctor)) return false;
        return id != null && id.equals(doctor.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
