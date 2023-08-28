package com.example.doctorconsultationapp.model

data class Doctor(
    val doctorId: String,
    val doctorName: String,
    val doctorMobile: String,
    val doctorEmail: String,
    val doctorDescription: String,
    val doctorPassword: String,
    val imageURL: String
    );