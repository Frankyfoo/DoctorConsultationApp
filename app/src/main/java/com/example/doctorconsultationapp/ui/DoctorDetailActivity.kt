package com.example.doctorconsultationapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.example.doctorconsultationapp.R
import com.example.doctorconsultationapp.api.DoctorApi
import com.example.doctorconsultationapp.databinding.ActivityDoctorDetailBinding
import com.example.doctorconsultationapp.model.Doctor
import com.example.doctorconsultationapp.ui.fragment.DoctorListFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DoctorDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val doctorId = intent.getStringExtra("doctorId")
        val doctorImage =binding.imgDoctorDetail
        val doctorName = binding.tvDoctorNameDetail
        val doctorMobileNumber = binding.tvMobileNumber
        val doctorEmail = binding.tvEmail
        val doctorDescription = binding.tvDescription

        if (doctorId != null) {
            DoctorApi.retrofitService.getDoctorById(doctorId).enqueue(object : Callback<Doctor?> {
                override fun onResponse(call: Call<Doctor?>, response: Response<Doctor?>) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        Glide.with(this@DoctorDetailActivity).load(responseBody.imageURL).into(doctorImage)
                        doctorName.text = responseBody.doctorName
                        doctorMobileNumber.text = responseBody.doctorMobile
                        doctorEmail.text = responseBody.doctorEmail
                        doctorDescription.text = responseBody.doctorDescription
                    }
                }

                override fun onFailure(call: Call<Doctor?>, t: Throwable) {
                    Toast.makeText(this@DoctorDetailActivity, t.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        }

        binding.btnBookAppointment.setOnClickListener {
            val intent = Intent(this@DoctorDetailActivity, AppointmentConfirmationActivity::class.java)
            intent.putExtra("doctorId", doctorId)
            startActivity(intent)
        }
    }
}