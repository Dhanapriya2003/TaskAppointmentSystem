// ----------------------
// Helper Functions
// ----------------------
const API_BASE_URL = "https://appointmentsystem-wu2g.onrender.com/appointmentsystem";
// const API_BASE_URL = "https://guvi-k2tt.onrender.com/api";


// Load available doctor slots (unfiltered)
function loadAvailableSlots() {
  fetch(`${API_BASE_URL}/doctors`)
    .then(response => response.json())
    .then(doctors => {
      console.log("Doctors loaded:", doctors);
      const slotsList = document.getElementById('slotsList');
      if (!slotsList) {
        console.error("Element with id 'slotsList' not found.");
        return;
      }
      slotsList.innerHTML = '';
      doctors.forEach(doctor => {
        const slots = doctor.availableSlots || [];
        let slotsHtml = '';
        slots.forEach(slot => {
          slotsHtml += `<div>
                          <span>${slot}</span>
                          <button class="btn btn-sm btn-success ml-2" onclick="bookSlot('${doctor.id}', '${slot}')">Book Appointment</button>
                        </div>`;
        });
        slotsList.innerHTML += `<div class="card mb-3">
                                  <div class="card-body">
                                    <h5>${doctor.name} - ${doctor.specialization}</h5>
                                    ${slotsHtml}
                                  </div>
                                </div>`;
      });
    })
    .catch(error => console.error('Error loading slots:', error));
}

// Load available slots filtered by specialization.
function loadAvailableSlotsBySpecialization(specialization) {
  fetch(`${API_BASE_URL}/doctors`)
    .then(response => response.json())
    .then(doctors => {
      console.log("Doctors loaded:", doctors);
      if (specialization) {
        doctors = doctors.filter(doctor => doctor.specialization === specialization);
      }
      const slotsList = document.getElementById('slotsList');
      if (!slotsList) {
        console.error("Element with id 'slotsList' not found.");
        return;
      }
      slotsList.innerHTML = '';
      doctors.forEach(doctor => {
        const slots = doctor.availableSlots || [];
        let slotsHtml = '';
        slots.forEach(slot => {
          slotsHtml += `<div>
                          <span>${slot}</span>
                          <button class="btn btn-sm btn-success ml-2" onclick="bookSlot('${doctor.id}', '${slot}')">Book Appointment</button>
                        </div>`;
        });
        slotsList.innerHTML += `<div class="card mb-3">
                                  <div class="card-body">
                                    <h5>${doctor.name} - ${doctor.specialization}</h5>
                                    ${slotsHtml}
                                  </div>
                                </div>`;
      });
    })
    .catch(error => console.error('Error loading slots:', error));
}

// Book an appointment
function bookSlot(doctorId, slot) {
  let patientId = localStorage.getItem('patientId');
  if (!patientId) {
    patientId = prompt("Enter your Patient ID to book this slot:");
  }
  if (!patientId) return;
  const appointmentData = {
    patientId: patientId,
    doctorId: doctorId,
    appointmentTime: slot
  };

  fetch(`${API_BASE_URL}/appointments`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(appointmentData)
  })
    .then(response => response.json())
    .then(data => {
      alert('Appointment booked successfully!');
      loadAvailableSlots();
    })
    .catch(error => console.error('Error booking appointment:', error));
}

function loadPatientAppointments(patientId) {
  fetch(`${API_BASE_URL}/appointments/patient/${patientId}`)
    .then(response => response.json())
    .then(appointments => {
      let tableHtml = `<table class="table table-bordered">
                         <thead>
                           <tr>
                             <th>Booking ID</th>
                             <th>Doctor Name</th>
                             <th>Appointment Time</th>
                             <th>Medications</th>
                           </tr>
                         </thead>
                         <tbody>`;
      appointments.forEach(app => {
        const doctorName = doctorMapping[app.doctorId] || app.doctorId;
        tableHtml += `<tr>
                        <td>${app.id}</td>
                        <td>${doctorName}</td>
                        <td>${app.appointmentTime}</td>
                        <td>
                          <button class="btn btn-sm btn-info" onclick="viewMedications('${app.id}', '${patientId}')">
                            View Medications
                          </button>
                        </td>
                      </tr>`;
      });
      tableHtml += `</tbody></table>`;
      document.getElementById('appointmentsTable').innerHTML = tableHtml;
    })
    .catch(error => console.error('Error loading appointments:', error));
}

function viewMedications(appointmentId, patientId) {
  window.location.href = "patient_medications.html?appointmentId=" + appointmentId + "&patientId=" + patientId;
}


// Load medications for a patient (for patient view)
function loadMedications(patientId) {
  fetch(`${API_BASE_URL}/medications/patient/${patientId}`)
    .then(response => response.json())
    .then(medications => {
      let tableHtml = `<table class="table table-bordered"><thead><tr>
                         <th>Medication ID</th><th>Name</th><th>Dosage</th>
                         </tr></thead><tbody>`;
      medications.forEach(med => {
        tableHtml += `<tr>
                        <td>${med.id}</td>
                        <td>${med.medicationName}</td>
                        <td>${med.dosage}</td>
                      </tr>`;
      });
      tableHtml += `</tbody></table>`;
      document.getElementById('medicationsTable').innerHTML = tableHtml;
    })
    .catch(error => console.error('Error loading medications:', error));
}

// Load medications for doctor management (by patient)
function loadMedicationsForDoctor(patientId) {
  fetch(`${API_BASE_URL}/medications/patient/${patientId}`)
    .then(response => response.json())
    .then(medications => {
      let tableHtml = `<table class="table table-bordered"><thead><tr>
                         <th>Medication ID</th>
                         <th>Name</th>
                         <th>Dosage</th>
                         <th>Actions</th>
                         </tr></thead><tbody>`;
      medications.forEach(med => {
        tableHtml += `<tr>
                        <td>${med.id}</td>
                        <td>${med.medicationName}</td>
                        <td>${med.dosage}</td>
                        <td>
                          <button class="btn btn-sm btn-primary" onclick="editMedication('${med.id}', '${med.medicationName}', '${med.dosage}')">Edit</button>
                          <button class="btn btn-sm btn-danger" onclick="deleteMedication('${med.id}', '${patientId}')">Delete</button>
                        </td>
                      </tr>`;
      });
      tableHtml += `</tbody></table>`;
      document.getElementById('medicationsTable').innerHTML = tableHtml;
    })
    .catch(error => console.error('Error loading medications:', error));
}

// Load medications for a specific appointment (for doctor management)
function loadMedicationsForAppointment(appointmentId) {
  fetch(`${API_BASE_URL}/medications/appointment/${appointmentId}`)
    .then(response => response.json())
    .then(medications => {
      let tableHtml = `<table class="table table-bordered"><thead><tr>
                         <th>Medication ID</th>
                         <th>Name</th>
                         <th>Dosage</th>
                         <th>Days</th>
                         <th>Actions</th>
                         </tr></thead><tbody>`;
      medications.forEach(med => {
        tableHtml += `<tr>
                        <td>${med.id}</td>
                        <td>${med.medicationName}</td>
                        <td>${med.dosage}</td>
                        <td>${med.days || ''}</td>
                        <td>
                          <button class="btn btn-sm btn-primary" onclick="editMedicationForAppointment('${med.id}', '${med.medicationName}', '${med.dosage}', '${med.days || ''}')">Edit</button>
                          <button class="btn btn-sm btn-danger" onclick="deleteMedicationForAppointment('${med.id}', '${appointmentId}')">Delete</button>
                        </td>
                      </tr>`;
      });
      tableHtml += `</tbody></table>`;
      document.getElementById('medicationsTable').innerHTML = tableHtml;
    })
    .catch(error => console.error('Error loading medications for appointment:', error));
}

// For editing medication (shared for doctor management)
function editMedication(medicationId, medicationName, dosage) {
  document.getElementById('medicationId').value = medicationId;
  document.getElementById('medicationName').value = medicationName;
  document.getElementById('dosage').value = dosage;
}

// For editing medication for a specific appointment (includes days)
function editMedicationForAppointment(medicationId, medicationName, dosage, days) {
  editMedication(medicationId, medicationName, dosage);
  document.getElementById('days').value = days;
}

// For deleting medication (shared)
function deleteMedication(medicationId, patientId) {
  if (confirm("Are you sure you want to delete this medication?")) {
    fetch(`${API_BASE_URL}/medications/${medicationId}`, {
      method: 'DELETE'
    })
      .then(() => {
        alert("Medication deleted successfully!");
        loadMedicationsForDoctor(patientId);
      })
      .catch(error => console.error('Error deleting medication:', error));
  }
}

// For deleting medication for a specific appointment
function deleteMedicationForAppointment(medicationId, appointmentId) {
  if (confirm("Are you sure you want to delete this medication?")) {
    fetch(`${API_BASE_URL}/medications/${medicationId}`, {
      method: 'DELETE'
    })
      .then(() => {
        alert("Medication deleted successfully!");
        loadMedicationsForAppointment(appointmentId);
      })
      .catch(error => console.error('Error deleting medication:', error));
  }
}

// ----------------------
// Medication form submission (for doctor management)
// ----------------------
const medicationForm = document.getElementById('medicationForm');
if (medicationForm) {
  medicationForm.addEventListener('submit', function(e) {
    e.preventDefault();
    const medicationId = document.getElementById('medicationId').value;
    const appointmentId = localStorage.getItem('currentAppointmentId');
    const patientId = localStorage.getItem('doctorPatientId');
    const medicationData = {
      patientId: patientId,
      medicationName: document.getElementById('medicationName').value,
      dosage: document.getElementById('dosage').value,
      days: parseInt(document.getElementById('days').value, 10)
    };
    if (appointmentId) {
      medicationData.appointmentId = appointmentId;
    }
    if (medicationId) {
      // Update existing medication.
      fetch(`${API_BASE_URL}/medications/${medicationId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(medicationData)
      })
        .then(response => response.json())
        .then(data => {
          document.getElementById('medicationMessage').innerText = 'Medication updated successfully!';
          medicationForm.reset();
          document.getElementById('medicationId').value = '';
          if (appointmentId) {
            loadMedicationsForAppointment(appointmentId);
          } else {
            loadMedicationsForDoctor(patientId);
          }
        })
        .catch(error => {
          console.error('Error updating medication:', error);
          document.getElementById('medicationMessage').innerText = 'Failed to update medication!';
        });
    } else {
      // Create new medication.
      fetch(`${API_BASE_URL}/medications`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(medicationData)
      })
        .then(response => response.json())
        .then(data => {
          document.getElementById('medicationMessage').innerText = 'Medication added successfully!';
          medicationForm.reset();
          if (appointmentId) {
            loadMedicationsForAppointment(appointmentId);
          } else {
            loadMedicationsForDoctor(patientId);
          }
        })
        .catch(error => {
          console.error('Error adding medication:', error);
          document.getElementById('medicationMessage').innerText = 'Failed to add medication!';
        });
    }
  });
}

// Load medications for a specific appointment for patient view (read-only)
function loadMedicationsForAppointmentForPatient(appointmentId) {
  fetch(`${API_BASE_URL}/medications/appointment/${appointmentId}`)
    .then(response => response.json())
    .then(medications => {
      let tableHtml = `<table class="table table-bordered">
                         <thead>
                           <tr>
                             <th>Medication ID</th>
                             <th>Name</th>
                             <th>Dosage</th>
                             <th>Days</th>
                           </tr>
                         </thead>
                         <tbody>`;
      medications.forEach(med => {
        tableHtml += `<tr>
                        <td>${med.id}</td>
                        <td>${med.medicationName}</td>
                        <td>${med.dosage}</td>
                        <td>${med.days || ''}</td>
                      </tr>`;
      });
      tableHtml += `</tbody></table>`;
      document.getElementById('medicationsTable').innerHTML = tableHtml;
    })
    .catch(error => console.error('Error loading medications for appointment:', error));
}
