package com.Mafiuz04.medicalclinic.mapper;

import com.Mafiuz04.medicalclinic.model.*;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorDto mapToDto(Doctor doctor);

    List<DoctorDto> mapListToDto(List<Doctor> doctors);

}
