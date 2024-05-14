package com.Mafiuz04.medicalclinic.mapper;

import com.Mafiuz04.medicalclinic.model.Patient;
import com.Mafiuz04.medicalclinic.model.PatientDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientDto mapToDto(Patient patient);
    List<PatientDto> mapListToDto(List<Patient> patientList);
}
