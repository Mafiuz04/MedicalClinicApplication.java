package com.Mafiuz04.medicalclinic.repository;

import com.Mafiuz04.medicalclinic.model.ChangePassword;
import com.Mafiuz04.medicalclinic.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JPAPatientRepository extends JpaRepository<Patient,String> {

    default void updatePatient(Patient patient, Patient updatedPatient){
        patient.setBirthday(updatedPatient.getBirthday());
        patient.setPassword(updatedPatient.getPassword());
        patient.setFirstName(updatedPatient.getFirstName());
        patient.setLastName(updatedPatient.getLastName());
        patient.setEmail(updatedPatient.getEmail());
        patient.setPhoneNumber(updatedPatient.getPhoneNumber());
        patient.setIdCardNo(updatedPatient.getIdCardNo());
    }

   default void editPatientPassword(Patient patient, ChangePassword changedPassword){
       patient.setPassword(changedPassword.getPassword());
   }
}
