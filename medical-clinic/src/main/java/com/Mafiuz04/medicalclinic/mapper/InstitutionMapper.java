package com.Mafiuz04.medicalclinic.mapper;

import com.Mafiuz04.medicalclinic.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InstitutionMapper {
    InstitutionDto toDto(Institution institution);

    List<InstitutionDto> listToDto(List<Institution> institution);

    @Mapping(source = "medicalUser.firstName", target = "firstName")
    @Mapping(source = "medicalUser.lastName", target = "lastName")
    List<SimpleDoctorDto> simpleDoctorList(List<Doctor> doctors);

    @Mapping(source = "medicalUser.firstName", target = "firstName")
    @Mapping(source = "medicalUser.lastName", target = "lastName")
    SimpleDoctorDto simpleDoctor(Doctor doctors);
}
