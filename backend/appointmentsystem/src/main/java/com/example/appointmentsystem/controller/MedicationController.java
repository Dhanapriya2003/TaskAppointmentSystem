package com.example.appointmentsystem.controller;

import com.example.appointmentsystem.model.Medication;
import com.example.appointmentsystem.repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/appointmentsystem/medications")
@CrossOrigin(origins = "*")
public class MedicationController {

    @Autowired
    private MedicationRepository medicationRepository;

    @PostMapping
    public Medication addMedication(@RequestBody Medication medication) {
        return medicationRepository.save(medication);
    }

    @PutMapping("/{id}")
    public Medication updateMedication(@PathVariable String id, @RequestBody Medication medication) {
        medication.setId(id);
        return medicationRepository.save(medication);
    }

    @DeleteMapping("/{id}")
    public void deleteMedication(@PathVariable String id) {
        medicationRepository.deleteById(id);
    }

    @GetMapping("/patient/{patientId}")
    public List<Medication> getMedicationsByPatient(@PathVariable String patientId) {
        return medicationRepository.findByPatientId(patientId);
    }
    
    // New endpoint: medications for a specific appointment.
    @GetMapping("/appointment/{appointmentId}")
    public List<Medication> getMedicationsByAppointment(@PathVariable String appointmentId) {
        return medicationRepository.findByAppointmentId(appointmentId);
    }
}
