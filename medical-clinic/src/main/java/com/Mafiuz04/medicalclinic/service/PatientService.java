package com.Mafiuz04.medicalclinic.service;

import com.Mafiuz04.medicalclinic.exception.MedicalClinicException;
import com.Mafiuz04.medicalclinic.mapper.PatientMapper;
import com.Mafiuz04.medicalclinic.model.ChangePassword;
import com.Mafiuz04.medicalclinic.model.Patient;
import com.Mafiuz04.medicalclinic.model.PatientDto;
import com.Mafiuz04.medicalclinic.repository.PatientRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    private final PatientRepo patientRepo;
    private final PatientMapper patientMapper;

    public PatientService(PatientRepo patientRepo, PatientMapper patientMapper) {
        this.patientRepo = patientRepo;
        this.patientMapper = patientMapper;
    }

    public List<PatientDto> getPatients() {
        return patientMapper.mapListToDto(patientRepo.getPatients());
    }

    public PatientDto getPatientByEmail(String email) {
        return patientMapper.mapToDto(patientRepo.getPatient(email)
                .orElseThrow(() -> new MedicalClinicException("There is no patient with given email.", HttpStatus.BAD_REQUEST)));
    }

    public PatientDto addPatient(Patient patient) {
        checkData(patient);
        ifGivenEmailExist(patient);
        return patientMapper.mapToDto(patientRepo.createPatient(patient));
    }

    public void deletePatientByEmail(String email) {
        patientRepo.deletePatient(email);
    }

    public PatientDto updatePatientByMail(String email, Patient updatedPatient) {
        Patient patient = patientRepo.getPatient(email)
                .orElseThrow(() -> new MedicalClinicException("We can not update patient, wrong mail.", HttpStatus.BAD_REQUEST));
        isItExistingPatient(patient, updatedPatient);
        checkData(updatedPatient);
        idCardNumberVerification(patient, updatedPatient);
        patientRepo.editPatient(patient, updatedPatient);
        return patientMapper.mapToDto(patient);
    }

    public PatientDto changePatientPassword(String email, ChangePassword newPassword) {
        Patient patientByEmail = patientRepo.getPatient(email)
                .orElseThrow(() -> new MedicalClinicException("Wrong mail", HttpStatus.BAD_REQUEST));
        patientRepo.editPassword(patientByEmail, newPassword);
        return patientMapper.mapToDto(patientByEmail);
    }
//    public PatientDto convertToDto(Patient patient){
//        return new PatientDto(patient.getEmail(),patient.getFirstName()
//                ,patient.getLastName(),patient.getPhoneNumber(),patient.getBirthday());
//    }

    private void checkData(Patient patient) {
        if (patient.getEmail() == null || patient.getPassword() == null
                || patient.getBirthday() == null || patient.getFirstName() == null
                || patient.getLastName() == null || patient.getPhoneNumber() == null
                || patient.getIdCardNo() == null) {
            throw new MedicalClinicException("Please make sure that all data are included", HttpStatus.BAD_REQUEST);
        }
    }

    private void ifGivenEmailExist(Patient patient) {
        if (patientRepo.getPatients().stream()
                .anyMatch(patient1 -> patient1.getEmail().equals(patient.getEmail()))) {
            throw new MedicalClinicException("The patient with the provided e-mail address already exists in our system", HttpStatus.BAD_REQUEST);
        }
    }

    private void idCardNumberVerification(Patient patient, Patient updatedPatient) {
        if (!patient.getIdCardNo().equals(updatedPatient.getIdCardNo())) {
            throw new MedicalClinicException("You can not change ID card number.", HttpStatus.BAD_REQUEST);
        }
    }

    private void isItExistingPatient(Patient patient, Patient updatedPatient) {
        if (!patient.getEmail().equals(updatedPatient.getEmail())) {
            ifGivenEmailExist(updatedPatient);
        }
    }
}
