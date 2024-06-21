package com.Mafiuz04.medicalclinic.repository;

import com.Mafiuz04.medicalclinic.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JPADoctorRepository extends JpaRepository<Doctor, Long> {
}
