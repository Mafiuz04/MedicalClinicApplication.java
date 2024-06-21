package com.Mafiuz04.medicalclinic.mapper;

import com.Mafiuz04.medicalclinic.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientDto toDto(Patient patient);

    Patient createToPatient(PatientCreateDto patient);

    PatientCreateDto patientToCreate(Patient patient);

    List<PatientDto> listToDto(List<Patient> patientList);

    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "patient.id", target = "patientId")
    AppointmentDto appointmentToDto(Appointment appointment);

}
