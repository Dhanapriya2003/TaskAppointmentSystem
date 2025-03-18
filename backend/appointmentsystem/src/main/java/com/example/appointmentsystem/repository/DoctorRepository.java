package com.example.appointmentsystem.repository;

import com.example.appointmentsystem.model.Doctor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DoctorRepository extends MongoRepository<Doctor, String> {
    Doctor findByEmail(String email);
}
