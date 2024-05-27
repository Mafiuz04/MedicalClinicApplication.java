package com.Mafiuz04.medicalclinic.mapper;

import com.Mafiuz04.medicalclinic.model.Institution;
import com.Mafiuz04.medicalclinic.model.InstitutionDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InstitutionMapper {
    InstitutionDto mapToDto(Institution institution);

    List<InstitutionDto> mapListToDto(List<Institution> institution);
}
