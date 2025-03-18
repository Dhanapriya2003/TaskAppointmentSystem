package com.example.appointmentsystem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Document(collection = "patients")
public class Patient {
    @Id
    private String id;
    
    @NotBlank(message = "Name is mandatory")
    private String name;
    
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;
    
    @NotBlank(message = "Contact number is mandatory")
    private String contactNumber;
    
    private String medicalHistory;
    
    @NotBlank(message = "Password is mandatory")
    private String password;

    public Patient() { }

    public Patient(String name, String email, String contactNumber, String medicalHistory, String password) {
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
        this.medicalHistory = medicalHistory;
        this.password = password;
    }

    // Getters and Setters (omitted for brevity)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
