package com.example.appointmentsystem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "doctors")
public class Doctor {
    @Id
    private String id;
    private String name;
    private String specialization;
    private List<String> availableSlots; // Changed to List<String>
    private String email;
    private String password;

    public Doctor() { }

    public Doctor(String name, String specialization, List<String> availableSlots, String email, String password) {
        this.name = name;
        this.specialization = specialization;
        this.availableSlots = availableSlots;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public List<String> getAvailableSlots() { return availableSlots; }
    public void setAvailableSlots(List<String> availableSlots) { this.availableSlots = availableSlots; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
