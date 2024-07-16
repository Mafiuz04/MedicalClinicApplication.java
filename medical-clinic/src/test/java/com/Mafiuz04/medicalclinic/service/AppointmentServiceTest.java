package com.Mafiuz04.medicalclinic.service;

import com.Mafiuz04.medicalclinic.exception.MedicalClinicException;
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
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
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
    void createAppointment_StartDateInPast_ThrowException() {
        //given
        AppointmentDto appointmentDto = new AppointmentDto(1L, 1L, 1L, LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1));
        //when
        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class, () -> appointmentService.createAppointment(appointmentDto));
        //then
        Assertions.assertEquals("Appointment is no available any more, please check date.", exception.getMessage());
    }

    @Test
    void createAppointment_TimeIsNotAFullQuarter_ThrowException() {
        //given
        AppointmentDto appointmentDto = new AppointmentDto(1L, 1L, 1L, LocalDateTime.of(2026, 6, 16, 20, 20), LocalDateTime.of(2026, 6, 16, 21, 10));
        //when
        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class, () -> appointmentService.createAppointment(appointmentDto));
        //then
        Assertions.assertEquals("Please change hour, the doctor sees patient every 15 minutes", exception.getMessage());
    }

    @Test
    void createAppointment_EndDateIsIsEarlierThanStartDate_ThrowException() {
        //given
        AppointmentDto appointmentDto = new AppointmentDto(1L, 1L, 1L, LocalDateTime.of(2026, 6, 16, 20, 20), LocalDateTime.of(2026, 6, 16, 19, 10));
        //when
        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class, () -> appointmentService.createAppointment(appointmentDto));
        //then
        Assertions.assertEquals("please check ending time, it is earlier than start time", exception.getMessage());
    }

    @Test
    void createAppointment_OverLapping_ThrowException() {
        //given
        AppointmentDto appointmentDto = new AppointmentDto(1L, 1L, 1L, LocalDateTime.of(2026, 6, 16, 20, 15), LocalDateTime.of(2026, 6, 16, 21, 30));
        Appointment appointment = new Appointment();
        appointment.setDoctor(new Doctor());
        LocalDateTime startDate = LocalDateTime.of(2026, 6, 16, 20, 15);
        LocalDateTime endDate = LocalDateTime.of(2026, 6, 16, 21, 30);
        appointment.setStartDate(startDate);
        appointment.setEndDate(endDate);
        appointment.getDoctor().setId(1L);
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment);
        when(appointmentRepository.overlappingAppointments(appointment.getDoctor().getId(), startDate, endDate)).thenReturn(appointments);
        //when
        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class, () -> appointmentService.createAppointment(appointmentDto));
        //then
        Assertions.assertEquals("Time range already taken", exception.getMessage());
    }

    @Test
    void createAppointment_DoctorDoesNotExist_ThrowException() {
        //given
        AppointmentDto appointmentDto = new AppointmentDto(1L, 1L, 1L, LocalDateTime.of(2026, 6, 16, 20, 15), LocalDateTime.of(2026, 6, 16, 20, 30));
        Appointment appointment = new Appointment();
        appointment.setDoctor(new Doctor());
        LocalDateTime startDate = LocalDateTime.of(2026, 6, 16, 20, 30);
        LocalDateTime endDate = LocalDateTime.of(2026, 6, 16, 21, 45);
        appointment.setStartDate(startDate);
        appointment.setEndDate(endDate);
        appointment.getDoctor().setId(1L);
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment);
        when(appointmentRepository.overlappingAppointments(appointment.getDoctor().getId(), startDate, endDate)).thenReturn(appointments);
        //when
        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class, () -> appointmentService.createAppointment(appointmentDto));
        //then
        Assertions.assertEquals("Chosen Doctor does not exist", exception.getMessage());
    }

    @Test
    void assignPatient_CorrectData_ReturnAppointmentDto() {
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
        Assertions.assertEquals(2L, appointmentDto.getPatientId());
        Assertions.assertEquals(startDate, appointmentDto.getStartDate());
    }

    @Test
    void assignPatient_AppointmentDoesNotExist_ThrowsException() {
        //given
        Long id = 1L;
        Long patientId = 1L;
        //when
        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class, () -> appointmentService.assignPatient(id, patientId));
        //then
        Assertions.assertEquals("Chosen appointment does not exist", exception.getMessage());
    }

    @Test
    void assignPatient_AppointmentPassed_ThrowException() {
        //given
        Long id = 1L;
        Long patientId = 1L;
        Appointment appointment = new Appointment();
        appointment.setStartDate(LocalDateTime.now().minusHours(2));
        when(appointmentRepository.findById(id)).thenReturn(Optional.of(appointment));
        //when
        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class, () -> appointmentService.assignPatient(id, patientId));
        //then
        Assertions.assertEquals("Appointment is no available any more, please check date.", exception.getMessage());
    }

    @Test
    void assignPatient_PatientNotFound_ThrowException() {
        //given
        Long id = 1L;
        Long patientId = 1L;
        Appointment appointment = new Appointment();
        appointment.setStartDate(LocalDateTime.now().plusHours(2));
        when(appointmentRepository.findById(id)).thenReturn(Optional.of(appointment));
        //when
        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class, () -> appointmentService.assignPatient(id, patientId));
        //then
        Assertions.assertEquals("Chosen Patient does not exist", exception.getMessage());
    }

    @Test
    void getAppointments_AppointmentsExist_ReturnListOFAppointments() {
        //given
        List<Appointment> appointmentList = new ArrayList<>();
        Appointment appointment = new Appointment();
        appointmentList.add(appointment);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Appointment> page = new PageImpl<>(appointmentList);
        when(appointmentRepository.findAll(pageable)).thenReturn(page);
        //when
        List<AppointmentDto> appointments = appointmentService.getAppointments(pageable);
        //then
        Assertions.assertEquals(1, appointments.size());
        Assertions.assertNotNull(appointments);
        Assertions.assertEquals(appointments.getFirst(), appointmentMapper.toDto(appointment));
    }

    @Test
    void getDoctorAvailableAppointments_NoDoctor_ThrowException() {
        Long doctorId = 1L;
        when(doctorRepository.existsById(doctorId)).thenReturn(false);
        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class,
                () -> appointmentService.getDoctorAvailableAppointments(doctorId));
        Assertions.assertEquals("There is no Doctor with given Id", exception.getMessage());
    }

    @Test
    void getDoctorAvailableAppointments_NoAppointments_ThrowException() {
        Long doctorId = 1L;
        ArrayList<Appointment> appointments = new ArrayList<>();
        when(doctorRepository.existsById(doctorId)).thenReturn(true);
        when(appointmentRepository.getAvailableAppointmentsByDoctorId(doctorId)).thenReturn(appointments);
        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class,
                () -> appointmentService.getDoctorAvailableAppointments(doctorId));
        Assertions.assertEquals("There is no available appointments", exception.getMessage());
    }

    @Test
    void getDoctorAvailableAppointments_AppointmentsExist_ListReturned() {
        Long doctorId = 1L;
        ArrayList<Appointment> appointments = new ArrayList<>();
        appointments.add(new Appointment());
        appointments.add(new Appointment());
        appointments.add(new Appointment());
        when(doctorRepository.existsById(doctorId)).thenReturn(true);
        when(appointmentRepository.getAvailableAppointmentsByDoctorId(doctorId)).thenReturn(appointments);

        List<AppointmentDto> doctorAvailableAppointments = appointmentService.getDoctorAvailableAppointments(doctorId);

        Assertions.assertEquals(3, doctorAvailableAppointments.size());
        Assertions.assertNotNull(doctorAvailableAppointments);
        Assertions.assertFalse(doctorAvailableAppointments.isEmpty());
    }

    @Test
    void getPatientAppointments_NoPatient_ThrowException() {
        Long patientId = 1L;
        when(patientRepository.existsById(patientId)).thenReturn(false);
        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class,
                () -> appointmentService.getPatientAppointments(patientId));
        Assertions.assertEquals("There is no patient with given Id", exception.getMessage());
    }

    @Test
    void getPatientAppointments_NoAppointments_ThrowException() {
        Long patientId = 1L;
        ArrayList<Appointment> appointments = new ArrayList<>();
        when(patientRepository.existsById(patientId)).thenReturn(true);
        when(appointmentRepository.getAppointmentsById(patientId)).thenReturn(appointments);
        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class,
                () -> appointmentService.getPatientAppointments(patientId));
        Assertions.assertEquals("There is no assign appointments", exception.getMessage());
    }

    @Test
    void getPatientAppointments_AppointmentsExist_ListReturned() {
        Long patientId = 1L;
        ArrayList<Appointment> appointments = new ArrayList<>();
        appointments.add(new Appointment());
        appointments.add(new Appointment());
        appointments.add(new Appointment());
        when(patientRepository.existsById(patientId)).thenReturn(true);
        when(appointmentRepository.getAppointmentsById(patientId)).thenReturn(appointments);

        List<AppointmentDto> patientAppointments = appointmentService.getPatientAppointments(patientId);

        Assertions.assertEquals(3, patientAppointments.size());
        Assertions.assertNotNull(patientAppointments);
        Assertions.assertFalse(patientAppointments.isEmpty());
    }

    @Test
    void getAvailableAppointmentsBySpecialization_ExistingAppointments_ReturnAvailableAppointments() {
        String specialization = "Cardiology";
        LocalDate date = LocalDate.now();
        List<Appointment> appointments = List.of(new Appointment());
        List<AppointmentDto> appointmentDtos = appointmentMapper.mapListToDto(appointments);
        when(appointmentRepository.getAvailableAppointmentsBySpecialization(specialization, date)).thenReturn(appointments);

        List<AppointmentDto> result = appointmentService.getAvailableAppointmentsBySpecialization(specialization, date);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(appointmentDtos, result);
    }

    @Test
    void getAvailableAppointmentsBySpecialization_NoAvailableAppointments() {
        String specialization = "Dermatology";
        LocalDate date = LocalDate.now();
        when(appointmentRepository.getAvailableAppointmentsBySpecialization(specialization, date)).
                thenReturn(List.of());

        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class, () ->
                appointmentService.getAvailableAppointmentsBySpecialization(specialization, date)
        );

        Assertions.assertEquals("There is no appointments", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
}
