package com.Mafiuz04.medicalclinic.repository;

import com.Mafiuz04.medicalclinic.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    @Query("select a " +
            "from Appointment a " +
            "where a.patient.id = :patientId " +
            "and a.patient.id is not null")
    List<Appointment> getAppointmentsById(Long patientId);

    @Query("select a " +
            "from Appointment a " +
            "where a.doctor.id = :doctorId " +
            "and a.patient.id is null ")
    List<Appointment> getAvailableAppointmentsByDoctorId(Long doctorId);

    @Query("select a " +
            "from Appointment a " +
            "where a.doctor.specialization = :specialization " +
            "and FUNCTION('DATE', a.startDate) = :date " +
            "and a.patient.id is null")
    List<Appointment> getAvailableAppointmentsBySpecialization(String specialization, LocalDate date);
}
