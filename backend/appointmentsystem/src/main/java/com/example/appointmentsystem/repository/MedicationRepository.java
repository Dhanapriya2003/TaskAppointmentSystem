package com.example.appointmentsystem.repository;

import com.example.appointmentsystem.model.Medication;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MedicationRepository extends MongoRepository<Medication, String> {
    List<Medication> findByPatientId(String patientId);
    List<Medication> findByAppointmentId(String appointmentId);
}
