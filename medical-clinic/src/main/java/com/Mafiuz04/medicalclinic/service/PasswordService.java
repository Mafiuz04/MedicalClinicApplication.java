package com.Mafiuz04.medicalclinic.service;

import com.Mafiuz04.medicalclinic.model.Password;
import com.Mafiuz04.medicalclinic.model.Patient;
import com.Mafiuz04.medicalclinic.repository.PatientRepo;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;
@Service
public class PasswordService {

    PatientRepo patientRepo;

    public PasswordService(PatientRepo patientRepo) {
        this.patientRepo = patientRepo;
    }

    public Patient changePassword(String email, Password newPassword) {
        Patient patient = patientRepo.getPatient(email)
                .orElseThrow(() -> new NoSuchElementException("Patient with specific email, can not be found"));
        patient.setPassword(newPassword);
        return patient;
    }
}
