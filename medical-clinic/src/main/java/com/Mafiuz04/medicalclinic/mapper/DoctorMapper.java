package com.Mafiuz04.medicalclinic.mapper;

import com.Mafiuz04.medicalclinic.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorDto tooDto(Doctor doctor);

    List<DoctorDto> listToDto(List<Doctor> doctors);

    Doctor createToDoctor(DoctorCreateDto doctor);

    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "patient.id", target = "patientId")
    AppointmentDto appointmentToDto(Appointment appointment);
}
