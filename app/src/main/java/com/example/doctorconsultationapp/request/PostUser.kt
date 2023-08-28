package com.example.doctorconsultationapp.request

data class PostUser(
    val userName: String,
    val userMobile: String,
    val userEmail: String,
    val userPassword: String,
)