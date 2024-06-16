package com.Mafiuz04.medicalclinic.service;

import com.Mafiuz04.medicalclinic.mapper.AppointmentMapper;
import com.Mafiuz04.medicalclinic.model.*;
import com.Mafiuz04.medicalclinic.repository.JPAAppointmentRepository;
import com.Mafiuz04.medicalclinic.repository.JPADoctorRepository;
import com.Mafiuz04.medicalclinic.repository.JPAPatientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AppointmentServiceTest {
    JPADoctorRepository doctorRepository;
    JPAAppointmentRepository appointmentRepository;
    AppointmentMapper appointmentMapper;
    JPAPatientRepository patientRepository;
    AppointmentService appointmentService;

    @BeforeEach
    void setup() {
        this.doctorRepository = Mockito.mock(JPADoctorRepository.class);
        this.appointmentRepository = Mockito.mock(JPAAppointmentRepository.class);
        this.appointmentMapper = Mappers.getMapper(AppointmentMapper.class);
        this.patientRepository = Mockito.mock(JPAPatientRepository.class);
        this.appointmentService = new AppointmentService(doctorRepository, appointmentRepository, appointmentMapper, patientRepository);
    }

    @Test
    void createAppointment_CorrectData_ReturnAppointmentDto() {
        //given
        LocalDateTime startDate = LocalDateTime.now().plusHours(1).withMinute(15);
        LocalDateTime endDate = LocalDateTime.now().plusHours(2).withMinute(15);
        AppointmentDto appointmentDto = new AppointmentDto(1L, 1L, 1L, startDate, endDate);
        MedicalUser user = new MedicalUser(1L, "Adam", "Marczyk", "asd@", "password");
        Appointment entity = appointmentMapper.toEntity(appointmentDto);
        Doctor doctor = new Doctor(1L, "Cardio", new ArrayList<>(), new ArrayList<>(), user);
        entity.setDoctor(doctor);
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(appointmentRepository.save(any())).thenReturn(entity);
        //when
        AppointmentDto appointment = appointmentService.createAppointment(appointmentDto);
        //then
        verify(appointmentRepository).save(entity);
        Assertions.assertEquals(1, appointment.getId());
    }
    @Test
    void assignPatient_CorrectData_ReturnAppointmentDto(){
        //given
        Long appointmentId = 1L;
        Long patientId = 2L;
        LocalDateTime startDate = LocalDateTime.now().plusHours(1).withMinute(15);
        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setStartDate(startDate);
        Patient patient = new Patient();
        patient.setId(patientId);
        appointment.setPatient(patient);
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        //when
        AppointmentDto appointmentDto = appointmentService.assignPatient(appointmentId, patientId);
        //then
        Assertions.assertNotNull(appointment.getPatient());
        Assertions.assertEquals(2L,appointmentDto.getPatientId());
    }
    @Test
    void getAppointments_AppointmentsExist_ReturnListOFAppointments(){
        //given
        List<Appointment> appointmentList = new ArrayList<>();
        Appointment appointment = new Appointment();
        appointmentList.add(appointment);
        Pageable pageable = PageRequest.of(0,10);
        Page<Appointment> page = new PageImpl<>(appointmentList);
        when(appointmentRepository.findAll(pageable)).thenReturn(page);
        //when
        List<AppointmentDto> appointments = appointmentService.getAppointments(pageable);
        //then
        Assertions.assertEquals(1,appointments.size());
        Assertions.assertNotNull(appointments);
    }
}
