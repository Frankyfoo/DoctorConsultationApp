package com.example.doctorconsultationapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.doctorconsultationapp.api.DoctorApi
import com.example.doctorconsultationapp.databinding.ActivityLoginBinding
import com.example.doctorconsultationapp.model.User
import com.example.doctorconsultationapp.sharedPreferences.LoginPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var session: LoginPreferences
    private var isLoggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        session = LoginPreferences(this)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

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

            DoctorApi.retrofitService.getUsers().enqueue(object: Callback<List<User>?> {
                override fun onResponse(
                    call: Call<List<User>?>,
                    response: Response<List<User>?>
                ) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        for (User in responseBody) {
                            if (User.userEmail == email && User.userPassword == password) {
                                isLoggedIn = true
                                Toast.makeText(this@LoginActivity, "Login Successfully", Toast.LENGTH_SHORT).show()
                                session.createLoginSession(User.userId.toString(), email)
                                var intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.putExtra("userName", User.userName)
                                intent.putExtra("userEmail", User.userEmail)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }

                    if (isLoggedIn == false) {
                        Toast.makeText(this@LoginActivity, "Incorrect Email or Password", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<User>?>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            })
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}