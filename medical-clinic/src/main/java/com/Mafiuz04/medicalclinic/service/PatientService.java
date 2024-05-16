package com.Mafiuz04.medicalclinic.service;

import com.Mafiuz04.medicalclinic.exception.MedicalClinicException;
import com.Mafiuz04.medicalclinic.mapper.PatientMapper;
import com.Mafiuz04.medicalclinic.model.ChangePassword;
import com.Mafiuz04.medicalclinic.model.Patient;
import com.Mafiuz04.medicalclinic.model.PatientDto;
import com.Mafiuz04.medicalclinic.repository.JPAPatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientMapper patientMapper;
    private final JPAPatientRepository patientRepository;

    public List<PatientDto> getPatients() {
        return patientMapper.mapListToDto(patientRepository.findAll());

    }

    public PatientDto getPatientByEmail(String email) {
        return patientMapper.mapToDto(patientRepository.findById(email)
                .orElseThrow(() -> new MedicalClinicException("There is no patient with given email.", HttpStatus.BAD_REQUEST)));
    }

    public PatientDto addPatient(Patient patient) {
        checkData(patient);
        ifGivenEmailExist(patient);
        ifGivenIdNumberExist(patient);
        return patientMapper.mapToDto(patientRepository.save(patient));
    }

    public void deletePatientByEmail(String email) {
        patientRepository.deleteById(email);
    }

    public PatientDto updatePatientByMail(String email, Patient updatedPatient) {
        Patient patient = patientRepository.findById(email)
                .orElseThrow(() -> new MedicalClinicException("We can not update patient, wrong mail.", HttpStatus.BAD_REQUEST));
        isItExistingPatient(patient, updatedPatient);
        checkData(updatedPatient);
        idCardNumberVerification(patient, updatedPatient);
        patientRepository.deleteById(email);
        update(patient, updatedPatient);
        patientRepository.save(patient);
        return patientMapper.mapToDto(patient);
    }

    public PatientDto changePatientPassword(String email, ChangePassword newPassword) {
        Patient patientByEmail = patientRepository.findById(email)
                .orElseThrow(() -> new MedicalClinicException("Wrong mail", HttpStatus.BAD_REQUEST));
        patientByEmail.setPassword(newPassword.getPassword());
        patientRepository.save(patientByEmail);
        return patientMapper.mapToDto(patientByEmail);
    }

    private void checkData(Patient patient) {
        if (patient.getEmail() == null || patient.getPassword() == null
                || patient.getBirthday() == null || patient.getFirstName() == null
                || patient.getLastName() == null || patient.getPhoneNumber() == null
                || patient.getIdCardNo() == null) {
            throw new MedicalClinicException("Please make sure that all data are included", HttpStatus.BAD_REQUEST);
        }
    }

    private void ifGivenEmailExist(Patient patient) {
        if (patientRepository.findAll().stream()
                .anyMatch(patient1 -> patient1.getEmail().equals(patient.getEmail()))) {
            throw new MedicalClinicException("The patient with the provided e-mail address already exists in our system", HttpStatus.BAD_REQUEST);
        }
    }
    private void ifGivenIdNumberExist(Patient patient) {
        if (patientRepository.findAll().stream()
                .anyMatch(patient1 -> patient1.getIdCardNo().equals(patient.getIdCardNo()))) {
            throw new MedicalClinicException("GIven ID number already exists in our system", HttpStatus.BAD_REQUEST);
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

    private void update(Patient patient, Patient updatedPatient) {
        patient.setBirthday(updatedPatient.getBirthday());
        patient.setPassword(updatedPatient.getPassword());
        patient.setFirstName(updatedPatient.getFirstName());
        patient.setLastName(updatedPatient.getLastName());
        patient.setEmail(updatedPatient.getEmail());
        patient.setPhoneNumber(updatedPatient.getPhoneNumber());
        patient.setIdCardNo(updatedPatient.getIdCardNo());
    }

}
