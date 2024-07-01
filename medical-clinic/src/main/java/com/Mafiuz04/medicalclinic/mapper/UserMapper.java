package com.Mafiuz04.medicalclinic.mapper;

import com.Mafiuz04.medicalclinic.model.MedicalUser;
import com.Mafiuz04.medicalclinic.model.UserDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(MedicalUser user);

    List<UserDto> listToDto(List<MedicalUser> users);
}
