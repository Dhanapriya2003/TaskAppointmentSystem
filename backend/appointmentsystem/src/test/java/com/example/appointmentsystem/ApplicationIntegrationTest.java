package com.example.appointmentsystem;

import com.example.appointmentsystem.model.Patient;
import com.example.appointmentsystem.model.Appointment;
import com.example.appointmentsystem.model.Medicine;
import com.example.appointmentsystem.repository.PatientRepository;
import com.example.appointmentsystem.repository.AppointmentRepository;
import com.example.appointmentsystem.repository.MedicineRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    // Stored IDs for later tests
    private static String patientId;
    private static String appointmentId;
    
    @Test
    @Order(1)
    public void testPatientRegistration() throws Exception {
        // Create a new patient
        Patient patient = new Patient("John Doe", "john@example.com", "1234567890", "password123");
        String response = mockMvc.perform(post("/api/patients/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();
        Patient createdPatient = objectMapper.readValue(response, Patient.class);
        patientId = createdPatient.getId();
    }
    
    @Test
    @Order(2)
    public void testPatientLogin() throws Exception {
        // Login using the registered patient's email and password
        Patient loginRequest = new Patient();
        loginRequest.setEmail("john@example.com");
        loginRequest.setPassword("password123");
        mockMvc.perform(post("/api/patients/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(patientId));
    }
    
    @Test
    @Order(3)
    public void testAppointmentCreation() throws Exception {
        // Create a new appointment for the logged-in patient
        Appointment appointment = new Appointment();
        appointment.setPatientId(patientId);
        appointment.setDoctorId("doc123");
        appointment.setAppointmentDate(LocalDateTime.now().plusDays(2).toString());
        appointment.setStatus("SCHEDULED");
        appointment.setDiagnosis("");
        appointment.setComments("");
        
        String response = mockMvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();
        Appointment createdAppointment = objectMapper.readValue(response, Appointment.class);
        appointmentId = createdAppointment.getId();
    }
    
    @Test
    @Order(4)
    public void testMedicineAddition() throws Exception {
        // Create a medicine record linked to the created appointment
        Medicine medicine = new Medicine();
        medicine.setAppointmentId(appointmentId);
        medicine.setName("Paracetamol");
        medicine.setDosage("500mg");
        medicine.setDuration("5 days");
        
        mockMvc.perform(post("/api/medicines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medicine)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }
    
    @Test
    @Order(5)
    public void testAppointmentHistory() throws Exception {
        // Retrieve appointment history for the patient
        mockMvc.perform(get("/api/appointments/patient/" + patientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
