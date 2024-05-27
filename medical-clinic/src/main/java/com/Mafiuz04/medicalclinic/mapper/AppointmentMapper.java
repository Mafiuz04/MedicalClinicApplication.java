package com.Mafiuz04.medicalclinic.mapper;

import com.Mafiuz04.medicalclinic.model.Appointment;
import com.Mafiuz04.medicalclinic.model.AppointmentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "patient.id", target = "patientId")
    AppointmentDto mapToDto(Appointment appointment);

    Appointment mapToEntity(AppointmentDto appointmentDto);

    List<AppointmentDto> mapListToDto(List<Appointment> appointments);

}
