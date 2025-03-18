package com.example.appointmentsystem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "medications")
public class Medication {
    @Id
    private String id;
    private String patientId;
    private String appointmentId;  // Link to the appointment
    private String medicationName;
    private String dosage;
    private int days; // Amount of days

    public Medication() { }

    public Medication(String patientId, String appointmentId, String medicationName, String dosage, int days) {
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.days = days;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }
    public String getMedicationName() { return medicationName; }
    public void setMedicationName(String medicationName) { this.medicationName = medicationName; }
    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    public int getDays() { return days; }
    public void setDays(int days) { this.days = days; }
}
