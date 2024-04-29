package com.Mafiuz04.medicalclinic.repository;

import com.Mafiuz04.medicalclinic.model.Patient;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PatientRepo {
    private final List<Patient> patients;

    public PatientRepo() {
        this.patients = new ArrayList<>();
    }

    public List<Patient> getPatients() {
        return new ArrayList<>(patients);
    }

    public Optional<Patient> getPatient(String email) {
        return patients.stream()
                .filter(patient -> email.equals(patient.getEmail()))
                .findFirst();
    }

    public Patient createPatient(Patient patient) {
        patients.add(patient);
        return patient;
    }

    public void deletePatient(String email) {
        patients.removeIf(patient -> patient.getEmail().equals(email));
    }

    public void editPatient(Patient patient, Patient newPatientData) {
        patient.setBirthday(newPatientData.getBirthday());
        patient.setEmail(newPatientData.getEmail());
        patient.setPassword(newPatientData.getPassword());
        patient.setLastName(newPatientData.getLastName());
        patient.setFirstName(newPatientData.getFirstName());
        patient.setIdCardNo(newPatientData.getIdCardNo());
        patient.setPhoneNumber(newPatientData.getPhoneNumber());
    }
}
