package com.Mafiuz04.medicalclinic.repository;

import com.Mafiuz04.medicalclinic.model.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JPAInstitutionRepository extends JpaRepository<Institution, Long> {
}
