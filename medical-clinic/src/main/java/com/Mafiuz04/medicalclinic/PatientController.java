package com.Mafiuz04.medicalclinic;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PatientController {
    private PatientService patientService;

    //    @Autowired -> nie ma potrzeby używania tej adnotacji, gdyż mamy tylko jeden konstruktor, w przypadku wielu kontruktorów,
//    adnotacja mówi springowi który kontruktor ma użyć
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/patients")
    public List<Patient> getPatients() {
        return patientService.getPatients();
    }

    @GetMapping("/patients")
    public Patient getPatientByEmail(@RequestParam String email) {
        return patientService.getPatientByEmail(email);
    }
}
