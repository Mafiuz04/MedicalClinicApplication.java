package com.Mafiuz04.medicalclinic.mapper;

import com.Mafiuz04.medicalclinic.model.Appointment;
import com.Mafiuz04.medicalclinic.model.AppointmentDto;
import com.Mafiuz04.medicalclinic.model.Patient;
import com.Mafiuz04.medicalclinic.model.PatientDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {

   PatientDto toDto(Patient patient);

    List<PatientDto> listToDto(List<Patient> patientList);
    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "patient.id", target = "patientId")
    AppointmentDto appointmentToDto(Appointment appointment);

}
