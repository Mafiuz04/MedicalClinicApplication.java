package com.Mafiuz04.medicalclinic.service;

import com.Mafiuz04.medicalclinic.model.ChangePassword;
import com.Mafiuz04.medicalclinic.model.Patient;
import com.Mafiuz04.medicalclinic.repository.PatientRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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
                .orElseThrow(() -> new NoSuchElementException("There is no patient with given email."));
    }

    public Patient addPatient(Patient patient) {
        return patientRepo.createPatient(patient);
    }

    public void deletePatientByEmail(String email) {
        patientRepo.deletePatient(email);
    }

    public Patient updatePatientByMail(String email, Patient updatedPatient) {
        Patient patient = patientRepo.getPatient(email)
                .orElseThrow(() -> new NoSuchElementException("There is no patient with given email."));
        patientRepo.editPatient(patient, updatedPatient);
        return patient;
    }

    public Patient changePatientPassword(String email, ChangePassword newPassword) {
        Patient patientByEmail = patientRepo.getPatient(email)
                .orElseThrow(() -> new NoSuchElementException("There is no patient with given email."));
        patientRepo.editPassword(patientByEmail, newPassword);
        return patientByEmail;
    }

}
