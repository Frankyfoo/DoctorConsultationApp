package com.example.doctorconsultationapp.request

data class UpdateUser(
    val userName: String,
    val userMobile: String,
    val userPassword: String,
)