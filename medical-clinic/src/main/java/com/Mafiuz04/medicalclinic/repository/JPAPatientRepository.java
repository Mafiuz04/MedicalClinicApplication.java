package com.Mafiuz04.medicalclinic.repository;

import com.Mafiuz04.medicalclinic.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JPAPatientRepository extends JpaRepository<Patient,String> {
}
