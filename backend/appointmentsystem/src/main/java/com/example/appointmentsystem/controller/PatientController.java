package com.example.appointmentsystem.controller;

import com.example.appointmentsystem.model.Patient;
import com.example.appointmentsystem.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/appointmentsystem/patients")
@CrossOrigin(origins = "*")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    @PostMapping("/register")
    public Patient registerPatient(@Valid @RequestBody Patient patient) {
        return patientRepository.save(patient);
    }

    @PostMapping("/login")
    public Patient loginPatient(@RequestBody Patient loginRequest) {
        Patient patient = patientRepository.findByEmail(loginRequest.getEmail());
        if (patient != null && patient.getPassword().equals(loginRequest.getPassword())) {
            return patient;
        }
        return null;
    }

    @GetMapping("/{id}")
    public Patient getPatient(@PathVariable String id) {
        return patientRepository.findById(id).orElse(null);
    }
}
