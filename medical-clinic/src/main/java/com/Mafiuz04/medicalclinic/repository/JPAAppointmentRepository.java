package com.Mafiuz04.medicalclinic.repository;

import com.Mafiuz04.medicalclinic.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JPAAppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("select a " +
            "from Appointment a " +
            "where :doctorId = a.doctor.id " +
            "and a.startDate <= :endDate " +
            "and a.endDate > :startDate ")
    List<Appointment> overlappingAppointments(Long doctorId, LocalDateTime startDate, LocalDateTime endDate);
}
