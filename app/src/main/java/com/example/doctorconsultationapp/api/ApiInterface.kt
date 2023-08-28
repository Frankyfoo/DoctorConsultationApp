package com.example.doctorconsultationapp.api

import com.example.doctorconsultationapp.model.Appointment
import com.example.doctorconsultationapp.model.Doctor
import com.example.doctorconsultationapp.model.User
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

const val BASE_URL = "http://doctorapi2-dev.us-east-1.elasticbeanstalk.com/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface ApiInterface {
    @GET("api/user")
    fun getUsers(): Call<List<User>>

    @GET("api/user/{id}")
    fun getUserById(@Path("id") id: Int): Call<User>

    @Headers("Content-Type: application/json")
    @PUT("api/user/{id}")
    fun updateUser(@Path("id") id: Int, @Body requestBody: RequestBody): Call<User>

    @GET("api/doctor")
    fun getDoctors(): Call<List<Doctor>>

    @GET("api/doctor/{id}")
    fun getDoctorById(@Path("id") id: String): Call<Doctor>

    @GET("api/doctor/specialtylist")
    fun loadSpecialtyList(): Call<List<String>>

    @GET("api/doctor/getdoctorbyspecialist/{specialty}")
    fun getDoctorsBySpecialty(@Path("specialty") specialty: String): Call<List<Doctor>>

    @GET("api/appointment/{id}")
    fun getAppointmentsByUserId(@Path("id") id: Int): Call<List<Appointment>>

    @Headers("Content-Type: application/json")
    @POST("api/user")
    fun createUser(@Body requestBody: RequestBody): Call<User>

    @Headers("Content-Type: application/json")
    @POST("api/appointment")
    fun createAppointment(@Body requestBody: RequestBody): Call<Appointment>

    @DELETE("api/appointment/{id}")
    fun cancelAppointmentById(@Path("id") id: Int): Call<Appointment>
}

object DoctorApi {
    val retrofitService : ApiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }
}