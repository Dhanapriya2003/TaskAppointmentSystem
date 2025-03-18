package com.example.appointmentsystem.controller;

import com.example.appointmentsystem.model.Doctor;
import com.example.appointmentsystem.model.Appointment;
import com.example.appointmentsystem.repository.DoctorRepository;
import com.example.appointmentsystem.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointmentsystem/doctors")
@CrossOrigin(origins = "*")
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;

    @PostMapping("/register")
    public Doctor registerDoctor(@RequestBody Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @PostMapping("/login")
    public Doctor loginDoctor(@RequestBody Doctor loginRequest) {
        Doctor doctor = doctorRepository.findByEmail(loginRequest.getEmail());
        if (doctor != null && doctor.getPassword().equals(loginRequest.getPassword())) {
            return doctor;
        }
        return null;
    }

    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
    
    @GetMapping("/{doctorId}/appointments")
    public List<Appointment> getAppointmentsForDoctor(@PathVariable String doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }
}
