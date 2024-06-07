package com.Mafiuz04.medicalclinic.service;

import com.Mafiuz04.medicalclinic.exception.MedicalClinicException;
import com.Mafiuz04.medicalclinic.mapper.PatientMapper;
import com.Mafiuz04.medicalclinic.model.Patient;
import com.Mafiuz04.medicalclinic.model.PatientCreateDto;
import com.Mafiuz04.medicalclinic.model.PatientDto;
import com.Mafiuz04.medicalclinic.repository.JPAPatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientMapper patientMapper;
    private final JPAPatientRepository patientRepository;

    //TC1: W przypadku istnienia listy pacjentów, zostaje ona zwrócona.
    public List<PatientDto> getPatients(Pageable pageable) {
        List<Patient> patients = patientRepository.findAll(pageable).getContent();
        return patientMapper.listToDto(patients);
    }

    //TC1: W przypadku gdy pacjent o danym ID istnieje, zostanie przemapowany i zwrcony.
    //TC2: W przypadku gdy pacjent o danym ID nie istnieje, zostanie rzucony wyjątek.
    public PatientDto getPatientById(Long id) {
        return patientMapper.toDto(patientRepository.findById(id)
                .orElseThrow(() -> new MedicalClinicException("There is no patient with given email.", HttpStatus.BAD_REQUEST)));
    }

    public PatientDto addPatient(PatientCreateDto patientCreateDto) {
//        checkData(patientCreateDto);
//        ifGivenEmailExist(patientCreateDto);
//        ifGivenIdNumberExist(patientCreateDto);
        Patient patient = patientMapper.createToPatient(patientCreateDto);
        return patientMapper.toDto(patientRepository.save(patient));
    }

    //TC1: W przypadku istnienia pacjenata o danym ID, zostanie on usnięty z reposytorium.
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    //TC1: W przypadku gdy pacient o danym ID istnieje, wszystkie dane są uzupełnione, numer dowodu nie został zmieniony
// , pacient jest usuwany i zapisywany na nowo z nowymi danymi.
    // TC2: W przypadku gry pacien to danym ID nie istnieje, zostanie rzucony wyjątek.
    //TC3:  Wprzypadku gdy pacjent o danym ID istnieje, ale brakuje danych zostanie rzucony wyjatek.
    //TC4: P przypadku gdy pacjent o danym ID istnieje, podane są wszystkie dane, ale został zmieniony nr dowodu, powienie zostać rzucony wyjatek.
    @Transactional
    public PatientDto updatePatient(Long id, Patient updatedPatient) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new MedicalClinicException("We can not update patient, wrong mail.", HttpStatus.BAD_REQUEST));
        isItExistingPatient(patient, updatedPatient);
        checkData(updatedPatient);
        idCardNumberVerification(patient, updatedPatient);
        patientRepository.deleteById(id);
        update(patient, updatedPatient);
        patientRepository.save(patient);
        return patientMapper.toDto(patient);
    }

    private void checkData(Patient patient) {
        if (patient.getMedicalUser().getEmail() == null || patient.getMedicalUser().getPassword() == null
                || patient.getBirthday() == null || patient.getMedicalUser().getFirstName() == null
                || patient.getMedicalUser().getLastName() == null || patient.getPhoneNumber() == null
                || patient.getIdCardNo() == null) {
            throw new MedicalClinicException("Please make sure that all data are included", HttpStatus.BAD_REQUEST);
        }
    }

    private void ifGivenEmailExist(Patient patient) {
        if (patientRepository.findAll().stream()
                .anyMatch(patient1 -> patient1.getMedicalUser().getEmail().equals(patient.getMedicalUser().getEmail()))) {
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
        if (!patient.getMedicalUser().getEmail().equals(updatedPatient.getMedicalUser().getEmail())) {
            ifGivenEmailExist(updatedPatient);
        }
    }

    private void update(Patient patient, Patient updatedPatient) {
        patient.setBirthday(updatedPatient.getBirthday());
        patient.getMedicalUser().setPassword(updatedPatient.getMedicalUser().getPassword());
        patient.getMedicalUser().setFirstName(updatedPatient.getMedicalUser().getFirstName());
        patient.getMedicalUser().setLastName(updatedPatient.getMedicalUser().getLastName());
        patient.getMedicalUser().setEmail(updatedPatient.getMedicalUser().getEmail());
        patient.setPhoneNumber(updatedPatient.getPhoneNumber());
        patient.setIdCardNo(updatedPatient.getIdCardNo());
    }

}
