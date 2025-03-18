package com.example.appointmentsystem.controller;

import com.example.appointmentsystem.model.Appointment;
import com.example.appointmentsystem.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/appointmentsystem/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @PostMapping
    public Appointment bookAppointment(@RequestBody Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @GetMapping("/patient/{patientId}")
    public List<Appointment> getAppointmentsByPatient(@PathVariable String patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }
    
    @GetMapping("/doctor/{doctorId}")
    public List<Appointment> getAppointmentsByDoctor(@PathVariable String doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }
    
    @PutMapping("/{id}/diagnosis")
    public Appointment updateAppointmentDiagnosis(@PathVariable String id, @RequestBody Map<String, String> updates) {
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        if (appointment != null) {
            appointment.setDiagnosis(updates.get("diagnosis"));
            appointment.setDoctorComments(updates.get("doctorComments"));
            return appointmentRepository.save(appointment);
        }
        return null;
    }
}
