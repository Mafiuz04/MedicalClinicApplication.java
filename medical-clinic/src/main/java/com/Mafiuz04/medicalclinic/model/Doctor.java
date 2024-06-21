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
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String specialization;
    @ManyToMany(mappedBy = "doctors")
    private List<Institution> institutions = new ArrayList<>();
    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private MedicalUser medicalUser;

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
