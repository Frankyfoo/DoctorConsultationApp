package com.example.doctorconsultationapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doctorconsultationapp.R
import com.example.doctorconsultationapp.adapter.DoctorAdapter
import com.example.doctorconsultationapp.api.DoctorApi
import com.example.doctorconsultationapp.model.Doctor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DoctorListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvDoctor)
        spinner = view.findViewById(R.id.spinnerDoctorSpecialty)

        var selectedSpecialty = ""

        // set adapter for dropdownlist
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, ArrayList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // populate dropdownlist
        DoctorApi.retrofitService.loadSpecialtyList().enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    adapter.clear()
                    adapter.addAll(response.body()?: emptyList())
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<String>?>, t: Throwable) {
                Log.e("API", "Error: ${t.message}")
            }
        })

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position)
                // Do something with the selected item
                selectedSpecialty = selectedItem.toString()
                // load list of doctors according to specialty selected
                DoctorApi.retrofitService.getDoctorsBySpecialty(selectedSpecialty).enqueue(object : Callback<List<Doctor>?> {
                    override fun onResponse(call: Call<List<Doctor>?>, response: Response<List<Doctor>?>) {
                        val data = response.body()
                        if (data != null) {
                            // set up your adapter and layoutManager here
                            val adapter = DoctorAdapter(data)
                            recyclerView.adapter = adapter
                            recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        }
                    }

                    override fun onFailure(call: Call<List<Doctor>?>, t: Throwable) {
                        Log.e("API", "Error: ${t.message}")
                    }
                })
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                DoctorApi.retrofitService.getDoctors().enqueue(object : Callback<List<Doctor>?> {
                    override fun onResponse(call: Call<List<Doctor>?>, response: Response<List<Doctor>?>) {
                        val data = response.body()
                        if (data != null) {
                            // set up your adapter and layoutManager here
                            val adapter = DoctorAdapter(data)
                            recyclerView.adapter = adapter
                            recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        }
                    }

                    override fun onFailure(call: Call<List<Doctor>?>, t: Throwable) {
                        Log.e("API", "Error: ${t.message}")
                    }
                })
            }
        }

        // load list of doctors
//        DoctorApi.retrofitService.getDoctors().enqueue(object : Callback<List<Doctor>?> {
//            override fun onResponse(call: Call<List<Doctor>?>, response: Response<List<Doctor>?>) {
//                val data = response.body()
//                if (data != null) {
//                    // set up your adapter and layoutManager here
//                    val adapter = DoctorAdapter(data)
//                    recyclerView.adapter = adapter
//                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
//                }
//            }
//
//            override fun onFailure(call: Call<List<Doctor>?>, t: Throwable) {
//                Log.e("API", "Error: ${t.message}")
//            }
//        })


//        if (!selectedSpecialty.isEmpty()){
//            // load list of doctors according to specialty selected
//            DoctorApi.retrofitService.getDoctorsBySpecialty(selectedSpecialty).enqueue(object : Callback<List<Doctor>?> {
//                override fun onResponse(call: Call<List<Doctor>?>, response: Response<List<Doctor>?>) {
//                    val data = response.body()
//                    if (data != null) {
//                        // set up your adapter and layoutManager here
//                        val adapter = DoctorAdapter(data)
//                        recyclerView.adapter = adapter
//                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//                    }
//                }
//
//                override fun onFailure(call: Call<List<Doctor>?>, t: Throwable) {
//                    Log.e("API", "Error: ${t.message}")
//                }
//            })
//        } else {
//            // load list of doctors
//            DoctorApi.retrofitService.getDoctors().enqueue(object : Callback<List<Doctor>?> {
//                override fun onResponse(call: Call<List<Doctor>?>, response: Response<List<Doctor>?>) {
//                    val data = response.body()
//                    if (data != null) {
//                        // set up your adapter and layoutManager here
//                        val adapter = DoctorAdapter(data)
//                        recyclerView.adapter = adapter
//                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//                    }
//                }
//
//                override fun onFailure(call: Call<List<Doctor>?>, t: Throwable) {
//                    Log.e("API", "Error: ${t.message}")
//                }
//            })
//        }
    }
}