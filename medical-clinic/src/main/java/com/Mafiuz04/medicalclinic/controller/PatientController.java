package com.Mafiuz04.medicalclinic.controller;


import com.Mafiuz04.medicalclinic.model.ChangePassword;
import com.Mafiuz04.medicalclinic.model.Patient;
import com.Mafiuz04.medicalclinic.model.PatientDto;
import com.Mafiuz04.medicalclinic.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/patients")
@RestController
public class PatientController {
    private final PatientService patientService;

    //    @Autowired -> nie ma potrzeby używania tej adnotacji, gdyż mamy tylko jeden konstruktor, w przypadku wielu kontruktorów,
//    adnotacja mówi springowi który kontruktor ma użyć
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping()
    public List<PatientDto> getPatients() {
        return patientService.getPatients().stream()
                .map(patientService::convertToDto)
                .toList();
    }

    @GetMapping("/{email}")
    public PatientDto getPatientByEmail(@PathVariable String email) {
        Patient patientByEmail = patientService.getPatientByEmail(email);
        return patientService.convertToDto(patientByEmail);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDto addPatient(@RequestBody Patient patient) {
        Patient addedPatient = patientService.addPatient(patient);
        return patientService.convertToDto(addedPatient);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@PathVariable String email) {
        patientService.deletePatientByEmail(email);
    }

    @PutMapping("/{email}")
    public PatientDto updatePatient(@PathVariable String email, @RequestBody Patient updatedPatient) {
        Patient updatePatientByMail = patientService.updatePatientByMail(email, updatedPatient);
        return patientService.convertToDto(updatePatientByMail);
    }

    @PatchMapping("/{email}")
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDto updatePassword(@PathVariable String email, @RequestBody ChangePassword newPassword) {
        Patient patientNewPassword = patientService.changePatientPassword(email, newPassword);
        return patientService.convertToDto(patientNewPassword);
    }
}
