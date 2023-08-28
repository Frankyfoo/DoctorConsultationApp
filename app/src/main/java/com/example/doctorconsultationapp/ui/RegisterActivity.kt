package com.example.doctorconsultationapp.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.doctorconsultationapp.R
import com.example.doctorconsultationapp.api.DoctorApi
import com.example.doctorconsultationapp.databinding.ActivityRegisterBinding
import com.example.doctorconsultationapp.model.User
import com.example.doctorconsultationapp.request.PostUser
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnRegister.setOnClickListener{
            val email = binding.etEmail.text.toString().trim();
            val password = binding.etPassword.text.toString().trim();
            val name = binding.etName.text.toString().trim();
            val mobileNumber = binding.etMobileNumber.text.toString().trim();

            // check if all is all fields contains value
            if (email.isEmpty()) {
                binding.etEmail.error = "Email required"
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.etPassword.error = "Password required"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }

            if (name.isEmpty()) {
                binding.etName.error = "Name required"
                binding.etName.requestFocus()
                return@setOnClickListener
            }

            if (mobileNumber.isEmpty()) {
                binding.etMobileNumber.error = "Mobile number required"
                binding.etMobileNumber.requestFocus()
                return@setOnClickListener
            }

            val user = PostUser(
                name, mobileNumber, email, password
            )

            checkEmail(email, makeGSONRequestBody(user))

        }

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

//    private fun showDatePicker() {
//        // Get current date and set it as the default date in the date picker
////        val calendar = Calendar.getInstance()
////        val year = calendar.get(Calendar.YEAR)
////        val month = calendar.get(Calendar.MONTH)
////        val day = calendar.get(Calendar.DAY_OF_MONTH)
//
//        // Create a new DatePickerDialog and show it
////        val datePickerDialog = DatePickerDialog(this, { view, year, month, dayOfMonth ->
////            // Handle the selected date
////            // This lambda will be called when the user selects a date in the dialog
////            val textViewDate = findViewById<TextView>(R.id.etDate)
////            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
////            val calendar = Calendar.getInstance()
////            calendar.set(year, month, dayOfMonth)
////            val dateString = dateFormat.format(calendar.time)
////            textViewDate.text = dateString
////        }, year, month, day)
////        datePickerDialog.show()
//    }

    fun createUser(requestBody: RequestBody) {
        DoctorApi.retrofitService.createUser(requestBody)
            .enqueue(object : Callback<User?> {
                override fun onResponse(
                    call: Call<User?>,
                    response: Response<User?>
                ) {
                    Toast.makeText(this@RegisterActivity, "Registered Successfully", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<User?>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun checkEmail(email: String, requestBody: RequestBody) {
        var checkedEmail = ""
        DoctorApi.retrofitService.getUsers().enqueue(object : Callback<List<User>?> {
            override fun onResponse(
                call: Call<List<User>?>,
                response: Response<List<User>?>
            ) {
                val responseBody = response.body()
                Log.d("RegisterActivity", "$responseBody")
                if (responseBody != null) {
                    for (User in responseBody) {
                        if (User.userEmail == email) {
                            checkedEmail = User.userEmail
                        }
                    }
                }
                if (checkedEmail != email) {
                    createUser(requestBody)
                    Toast.makeText(this@RegisterActivity, "Registered Successfully", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@RegisterActivity, "Email Already Exists", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<User>?>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Error", Toast.LENGTH_SHORT).show()
                Log.d("RegisterActivity", "${t.toString()}")
            }
        })
    }

    private fun makeGSONRequestBody(jsonObject: Any?): RequestBody {
        return RequestBody.create(
            MediaType.parse("multipart/form-data"),
            Gson().toJson(jsonObject))
    }
}