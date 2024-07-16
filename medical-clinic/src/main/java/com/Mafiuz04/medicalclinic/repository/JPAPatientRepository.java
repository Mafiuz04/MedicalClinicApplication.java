package com.Mafiuz04.medicalclinic.repository;

import com.Mafiuz04.medicalclinic.model.AppointmentDto;
import com.Mafiuz04.medicalclinic.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JPAPatientRepository extends JpaRepository<Patient,Long> {
    @Query("select a " +
            "from Appointment a " +
            "where a.patient.id = :patientId ")
    List<AppointmentDto> getAppointments(Long patientId);
}
