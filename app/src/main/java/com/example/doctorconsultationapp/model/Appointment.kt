package com.example.doctorconsultationapp.model

data class Appointment(
    var appointmentId: Int,
    var doctorId: String,
    var userId: Int,
    var appointmentDescription: String,
    var appointmentDateTime: String,
    var doctorName: String,
    var doctorImgURL: String
)