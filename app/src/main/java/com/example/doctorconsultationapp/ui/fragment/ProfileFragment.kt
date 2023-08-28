package com.example.doctorconsultationapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.doctorconsultationapp.R
import com.example.doctorconsultationapp.api.DoctorApi
import com.example.doctorconsultationapp.model.User
import com.example.doctorconsultationapp.request.UpdateUser
import com.example.doctorconsultationapp.sharedPreferences.LoginPreferences
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    lateinit var userName: EditText
    lateinit var userMobileNumber: EditText
    lateinit var userEmail: EditText
    lateinit var userPassword: EditText
    lateinit var btnSaveProfile: Button
    lateinit var session: LoginPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userName = view.findViewById(R.id.etName)
        userMobileNumber = view.findViewById(R.id.etMobileNumber)
        userEmail = view.findViewById(R.id.etEmail)
        userPassword = view.findViewById(R.id.etPassword)
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile)

        session = LoginPreferences(requireContext())
        val userId = session.getUserDetails()["userId"]!!.toInt()

        // this api is to load the user profile to the text fields
        DoctorApi.retrofitService.getUserById(userId).enqueue(object: Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                val user = response.body()
                if (user != null) {
                    userName.setText(user.userName)
                    userMobileNumber.setText(user.userMobile)
                    userEmail.setText(user.userEmail)
                    userPassword.setText(user.userPassword)
                }
            }

            override fun onFailure(call: Call<User?>, t: Throwable) {
                Toast.makeText(requireContext(), "Unexpected Error", Toast.LENGTH_SHORT).show()
            }

        })

        btnSaveProfile.setOnClickListener {
            if (userName.text.isEmpty() || userMobileNumber.text.isEmpty() || userEmail.text.isEmpty() || userPassword.text.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all the fields!", Toast.LENGTH_SHORT).show()
            } else {

                val updatedUserInformation = UpdateUser(userName.text.toString(), userMobileNumber.text.toString(), userPassword.text.toString())

                DoctorApi.retrofitService.updateUser(userId, makeGSONRequestBody(updatedUserInformation)).enqueue(object: Callback<User?> {
                    override fun onResponse(call: Call<User?>, response: Response<User?>) {
                        if(response.isSuccessful) {
                            Toast.makeText(requireContext(), "Updated Successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Bad Request", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<User?>, t: Throwable) {
                        Toast.makeText(requireContext(), "Unexpected Error", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    fun makeGSONRequestBody(jsonObject: Any?): RequestBody {
        return RequestBody.create(
            MediaType.parse("multipart/form-data"),
            Gson().toJson(jsonObject))
    }
}