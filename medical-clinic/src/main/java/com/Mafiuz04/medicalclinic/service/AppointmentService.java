package com.Mafiuz04.medicalclinic.service;

import com.Mafiuz04.medicalclinic.exception.MedicalClinicException;
import com.Mafiuz04.medicalclinic.mapper.AppointmentMapper;
import com.Mafiuz04.medicalclinic.model.*;
import com.Mafiuz04.medicalclinic.repository.JPAAppointmentRepository;
import com.Mafiuz04.medicalclinic.repository.JPADoctorRepository;
import com.Mafiuz04.medicalclinic.repository.JPAPatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AppointmentService {
    private final JPADoctorRepository doctorRepository;
    private final JPAAppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final JPAPatientRepository patientRepository;

    public AppointmentDto createAppointment(AppointmentDto appointment) {
        if (isInPast(appointment.getStartDate())) {
            throw new MedicalClinicException("Appointment is no available any more, please check date.", HttpStatus.BAD_REQUEST);
        }
        if (isItAQuarter(appointment.getStartDate(), appointment.getEndDate())) {
            throw new MedicalClinicException("Please change hour, the doctor sees patient every 15 minutes", HttpStatus.BAD_REQUEST);
        }
        isEmptySlot(appointment, appointment.getStartDate(), appointment.getEndDate());
        Doctor doctor = doctorRepository.findById(appointment.getDoctorId())
                .orElseThrow(() -> new MedicalClinicException("Chosen Doctor does not exist", HttpStatus.BAD_REQUEST));
        Appointment appointment1 = appointmentMapper.toEntity(appointment);
        appointment1.setDoctor(doctor);
        Appointment entity = appointmentRepository.save(appointment1);
        return appointmentMapper.toDto(entity);
    }

    public AppointmentDto assignPatient(Long id, Long patientId) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new MedicalClinicException("Chosen appointment does not exist", HttpStatus.BAD_REQUEST));
        if (isInPast(appointment.getStartDate())) {
            throw new MedicalClinicException("Appointment is no available any more, please check date.", HttpStatus.BAD_REQUEST);
        }
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new MedicalClinicException("Chosen Patient does not exist", HttpStatus.BAD_REQUEST));
        appointment.setPatient(patient);
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(updatedAppointment);
    }

    public List<AppointmentDto> getAppointments(Pageable pageable) {
        List<Appointment> appointments = appointmentRepository.findAll(pageable).getContent();
        return appointmentMapper.mapListToDto(appointments);
    }

    private boolean isInPast(LocalDateTime date) {
        return date.isBefore(LocalDateTime.now());
    }

    private boolean isItAQuarter(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            throw new MedicalClinicException("please check ending time, it is earlier than start time", HttpStatus.BAD_REQUEST);
        }
        return ((startDate.getMinute() - endDate.getMinute()) % 15 != 0);
    }

    private void isEmptySlot(AppointmentDto appointment, LocalDateTime startDate, LocalDateTime endDate) {
        List<Appointment> appointments = appointmentRepository.overlappingAppointments(appointment.getDoctorId(), startDate, endDate);
        if (!appointments.isEmpty()) {
            throw new MedicalClinicException("Time range already taken", HttpStatus.BAD_REQUEST);
        }
    }
}
