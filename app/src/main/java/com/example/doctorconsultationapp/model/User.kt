package com.example.doctorconsultationapp.model

import java.time.LocalDate
import java.util.Date

data class User(
    val userId: Int,
    val userName: String,
    val userMobile: String,
    val userEmail: String,
    val userPassword: String,
)