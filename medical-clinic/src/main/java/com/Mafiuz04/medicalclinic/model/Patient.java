package com.Mafiuz04.medicalclinic.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String idCardNo;
    private String phoneNumber;
    private LocalDate birthday;
    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private MedicalUser medicalUser;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient patient)) return false;
        return id != null && id.equals(patient.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
