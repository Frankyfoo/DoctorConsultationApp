package com.example.doctorconsultationapp.ui

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.doctorconsultationapp.R
import com.example.doctorconsultationapp.api.DoctorApi
import com.example.doctorconsultationapp.databinding.ActivityAppointmentConfirmationBinding
import com.example.doctorconsultationapp.model.Appointment
import com.example.doctorconsultationapp.request.PostAppointment
import com.example.doctorconsultationapp.sharedPreferences.LoginPreferences
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AppointmentConfirmationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentConfirmationBinding
    private lateinit var session: LoginPreferences

    private val availableTimeSlots = arrayOf("10:00", "11:00", "12:00", "13:00", "14:00")
    private var appointmentDate = ""
    private var appointmentTime = ""
    private var appointmentDescription = ""
    private var userId = ""
    private var doctorId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentConfirmationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        session = LoginPreferences(this)
        doctorId = intent.getStringExtra("doctorId")!!
        userId = session.getUserDetails()["userId"]!!


        loadTimeSlots()

        binding.buttonDatePicker.setOnClickListener {
            showDatePicker()
        }

        binding.btnConfirmBooking.setOnClickListener {
            appointmentDescription = binding.etDescription.text.toString()
            if (userId.isEmpty() || doctorId.isEmpty() || appointmentDate.isEmpty() || appointmentTime.isEmpty() || appointmentDescription.isEmpty()) {
                Toast.makeText(this@AppointmentConfirmationActivity, "Please Fill all the fields", Toast.LENGTH_SHORT).show()
            }
            else {
                val appointment = PostAppointment(doctorId, userId.toInt(), appointmentDate, appointmentTime, appointmentDescription)
                DoctorApi.retrofitService.createAppointment(makeGSONRequestBody(appointment))
                    .enqueue(object : Callback<Appointment?> {
                        override fun onResponse(
                            call: Call<Appointment?>,
                            response: Response<Appointment?>
                        ) {
                            // if timeslot is available, then
                            if(response.isSuccessful) {
                                Toast.makeText(this@AppointmentConfirmationActivity, "Book Successfully",
                                    Toast.LENGTH_SHORT).show()

                                val intent = Intent(this@AppointmentConfirmationActivity, MainActivity::class.java)
                                startActivity(intent)
                            }
                            // if timeslot has been booked by someone else
                            else {
                                Toast.makeText(this@AppointmentConfirmationActivity, "This timeslot has been booked by someone. Please try another date and time.",
                                    Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onFailure(call: Call<Appointment?>, t: Throwable) {
                            Toast.makeText(this@AppointmentConfirmationActivity, "Book Unsuccessfully",
                                Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }


    }

    private fun loadTimeSlots() {
        availableTimeSlots.forEach {timeSlot ->
            val view = layoutInflater.inflate(R.layout.doctor_time_slot, binding.horizontalTimeSlot, false)
            // reason to use findViewById is because doctor_time_slot layout is not included in ActivityConfirmationActivityBinding
            var buttonTimeSlot = view.findViewById<Button>(R.id.btnTimeConfirm)

            buttonTimeSlot.text = timeSlot
            binding.horizontalTimeSlot.addView(view)

            buttonTimeSlot.setOnClickListener {
                // to show time has been selected
                binding.tvTimeSelected.text = "Selected Time: ${buttonTimeSlot.text}"
                // to populate appointmentTime variable
                appointmentTime = buttonTimeSlot.text.toString()
            }
        }
    }

    private fun showDatePicker() {
        // Get current date and set it as the default date in the date picker
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create a new DatePickerDialog and show it
        val datePickerDialog = DatePickerDialog(this, { view, year, month, dayOfMonth ->
            // Handle the selected date
            // This lambda will be called when the user selects a date in the dialog
            val textViewDate = findViewById<TextView>(R.id.etDate)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val dateString = dateFormat.format(calendar.time)
            // to show date has been selected
            textViewDate.text = dateString
            // to populate appointmentDate variable for creating appointment
            appointmentDate = dateString
        }, year, month, day)
        datePickerDialog.show()
    }

    private fun makeGSONRequestBody(jsonObject: Any?): RequestBody {
        return RequestBody.create(
            MediaType.parse("multipart/form-data"),
            Gson().toJson(jsonObject))
    }

}
