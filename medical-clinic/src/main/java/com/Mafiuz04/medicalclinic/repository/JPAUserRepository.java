package com.Mafiuz04.medicalclinic.repository;

import com.Mafiuz04.medicalclinic.model.MedicalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JPAUserRepository extends JpaRepository<MedicalUser, Long> {
}
