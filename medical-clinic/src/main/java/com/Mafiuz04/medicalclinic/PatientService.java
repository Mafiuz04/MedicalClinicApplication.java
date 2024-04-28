package com.Mafiuz04.medicalclinic;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {
    List<Patient> patients = new ArrayList<>();

    public List<Patient> getPatients() {
        List<Patient> copyListOfPatients = patients;
        return copyListOfPatients;
    }
    public Patient getPatientByEmail(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findAny().
                get();
    }

    public void addPatient( Patient patient) {
        patients.add(patient);
    }

    public void deletePatientByEmail(String email) {
        patients.removeIf(patient -> patient.getEmail().equals(email));
    }

    public void updatePatientByMail(String email, Patient updatedPatient) {
        Patient patient = findByEmail(email);
        patient.setEmail(updatedPatient.getEmail());
        patient.setPassword(updatedPatient.getPassword());
        patient.setIdCardNo(updatedPatient.getIdCardNo());
        patient.setFirstName(updatedPatient.getFirstName());
        patient.setLastName(updatedPatient.getLastName());
        patient.setPhoneNumber(updatedPatient.getPhoneNumber());
        patient.setBirthday(updatedPatient.getBirthday());
    }

    private Patient findByEmail(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findAny()
                .get();
    }
}
