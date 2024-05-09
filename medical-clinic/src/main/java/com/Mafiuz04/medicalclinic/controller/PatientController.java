package com.Mafiuz04.medicalclinic.controller;


import com.Mafiuz04.medicalclinic.model.ChangePassword;
import com.Mafiuz04.medicalclinic.model.Patient;
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
    public List<Patient> getPatients() {
        return patientService.getPatients();
    }

    @GetMapping("/{email}")
    public Patient getPatientByEmail(@PathVariable String email) {
        return patientService.getPatientByEmail(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Patient addPatient(@RequestBody Patient patient) {
        return patientService.addPatient(patient);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@PathVariable String email) {
        patientService.deletePatientByEmail(email);
    }

    @PutMapping("/{email}")
    public Patient updatePatient(@PathVariable String email, @RequestBody Patient updatedPatient) {
        return patientService.updatePatientByMail(email, updatedPatient);
    }

    @PatchMapping("/{email}")
    @ResponseStatus(HttpStatus.CREATED)
    public Patient updatePassword(@PathVariable String email,@RequestBody ChangePassword newPassword) {
        return patientService.changePatientPassword(email, newPassword);
    }
}
