package com.example.doctorconsultationapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doctorconsultationapp.R
import com.example.doctorconsultationapp.adapter.AppointmentAdapter
import com.example.doctorconsultationapp.adapter.DoctorAdapter
import com.example.doctorconsultationapp.api.DoctorApi
import com.example.doctorconsultationapp.model.Appointment
import com.example.doctorconsultationapp.model.Doctor
import com.example.doctorconsultationapp.sharedPreferences.LoginPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppointmentHistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var session: LoginPreferences
    private var userId = ""
    private var populatedAppointment: ArrayList<Appointment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_appointment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvAppointmentHistory)
        session = LoginPreferences(requireContext())
        userId = session.getUserDetails()["userId"]!!

        // Todo-Note: this uses API chaining, which calls API inside API
        DoctorApi.retrofitService.getAppointmentsByUserId(userId.toInt()).enqueue(object : Callback<List<Appointment>?> {
            override fun onResponse(
                call: Call<List<Appointment>?>,
                response: Response<List<Appointment>?>
            ) {
                val appointments = response.body()
                if (appointments != null) {
                    appointments.forEach{appointment ->
                        // Todo-Note: from each of the appointment, get the doctorId and use it to get Doctor
                        DoctorApi.retrofitService.getDoctorById(appointment.doctorId).enqueue(object: Callback<Doctor?>{
                            override fun onResponse(
                                call: Call<Doctor?>,
                                response: Response<Doctor?>
                            ) {
                                val doctor = response.body()
                                if (doctor != null) {
                                    // Todo-Note: if the same then populate doctorName and doctorImage in appointment
                                    if(appointment.doctorId == doctor.doctorId) {
                                        appointment.doctorName = doctor.doctorName
                                        appointment.doctorImgURL = doctor.imageURL
                                        // todo-Note: items are loaded to the list one by one
                                        populatedAppointment.add(appointment)
                                    }
                                    // todo-Note: items are loaded to the list one by one
                                    val adapter = AppointmentAdapter(populatedAppointment)
                                    recyclerView.adapter = adapter
                                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                                }
                            }

                            override fun onFailure(call: Call<Doctor?>, t: Throwable) {
                                Log.e("API", "Error: ${t.message}")
                            }
                        })
                    }
                }
            }

            override fun onFailure(call: Call<List<Appointment>?>, t: Throwable) {
                Log.e("API", "Error: ${t.message}")
            }
        })
    }


}