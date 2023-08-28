package com.example.doctorconsultationapp.request

data class PostAppointment(
    val doctorId: String,
    val userId: Int,
    val appointmentDate: String,
    val appointmentTime: String,
    val appointmentDescription: String
)