package com.example.appointmentsystem.repository;

import com.example.appointmentsystem.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PatientRepository extends MongoRepository<Patient, String> {
    Patient findByEmail(String email);
}
