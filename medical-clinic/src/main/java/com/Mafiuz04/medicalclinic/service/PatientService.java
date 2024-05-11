package com.Mafiuz04.medicalclinic.service;

import com.Mafiuz04.medicalclinic.exception.MedicalClinicException;
import com.Mafiuz04.medicalclinic.model.ChangePassword;
import com.Mafiuz04.medicalclinic.model.Patient;
import com.Mafiuz04.medicalclinic.repository.PatientRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    private final PatientRepo patientRepo;

    public PatientService(PatientRepo patientRepo) {
        this.patientRepo = patientRepo;
    }

    public List<Patient> getPatients() {
        return patientRepo.getPatients();
    }

    public Patient getPatientByEmail(String email) {
        return patientRepo.getPatient(email)
                .orElseThrow(() -> new MedicalClinicException("There is no patient with given email.", HttpStatus.BAD_REQUEST));
    }

    public Patient addPatient(Patient patient) {
        checkData(patient);
        givenEmailExist(patient);
        return patientRepo.createPatient(patient);
    }

    public void deletePatientByEmail(String email) {
        patientRepo.deletePatient(email);
    }

    public Patient updatePatientByMail(String email, Patient updatedPatient) {
        Patient patient = patientRepo.getPatient(email)
                .orElseThrow(() -> new MedicalClinicException("We can not update patient, wrong mail.", HttpStatus.BAD_REQUEST));
        checkData(updatedPatient);
        idCardNumberVerification(patient,updatedPatient);
        patientRepo.editPatient(patient, updatedPatient);
        return patient;
    }

    public Patient changePatientPassword(String email, ChangePassword newPassword) {
        Patient patientByEmail = patientRepo.getPatient(email)
                .orElseThrow(() -> new MedicalClinicException("Wrong mail", HttpStatus.BAD_REQUEST));
        patientRepo.editPassword(patientByEmail, newPassword);
        return patientByEmail;
    }

    private void checkData(Patient patient) {
        if (patient.getEmail() == null || patient.getPassword() == null
                || patient.getBirthday() == null || patient.getFirstName() == null
                || patient.getLastName() == null || patient.getPhoneNumber() == null
                || patient.getIdCardNo() == null) {
            throw new MedicalClinicException("Please make sure that all data are included", HttpStatus.BAD_REQUEST);
        }
    }

    private void givenEmailExist(Patient patient) {
        if (patientRepo.getPatients().stream()
                .anyMatch(patient1 -> patient1.getEmail().equals(patient.getEmail()))) {
            throw new MedicalClinicException("The patient with the provided e-mail address already exists in our system", HttpStatus.BAD_REQUEST);
        }
    }
    private void idCardNumberVerification(Patient patient, Patient updatedPatient){
        if(!patient.getIdCardNo().equals(updatedPatient.getIdCardNo())){
            throw new MedicalClinicException("You can not change ID card number.",HttpStatus.FORBIDDEN);
        }
    }
}
