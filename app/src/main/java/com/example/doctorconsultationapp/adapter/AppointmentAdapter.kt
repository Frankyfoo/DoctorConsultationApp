package com.example.doctorconsultationapp.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doctorconsultationapp.R
import com.example.doctorconsultationapp.api.DoctorApi
import com.example.doctorconsultationapp.model.Appointment
import com.example.doctorconsultationapp.ui.DoctorDetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// , private val doctorNameParam: String, private val doctorImgURLParam: String

class AppointmentAdapter(private val appointments: ArrayList<Appointment>):
    RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    inner class AppointmentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val doctorImage: ImageView = itemView.findViewById(R.id.imgAppointmentDoctor)
        val doctorName: TextView = itemView.findViewById(R.id.tvAppointmentDoctorName)
        val appointmentDate: TextView = itemView.findViewById(R.id.tvAppointmentDate)
        val appointmentTime: TextView = itemView.findViewById(R.id.tvAppointmentTime)
        val appointmentId: TextView = itemView.findViewById(R.id.tvAppointmentId)
        val description: TextView = itemView.findViewById(R.id.tvDescription)
        val btnCancelAppointment: Button = itemView.findViewById(R.id.btnCancelAppointment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.appointment_list_item, parent, false)
        return AppointmentViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return appointments.size
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val currentAppointment = appointments[position]
        val dateTimeParts = currentAppointment.appointmentDateTime.split("T")

        val appointmentId: Int = currentAppointment.appointmentId
        Glide.with(holder.itemView.context)
            .load(currentAppointment.doctorImgURL)
            .into(holder.doctorImage)
        holder.appointmentId.text = "Appointment ID: ${currentAppointment.appointmentId}"
        holder.doctorName.text = currentAppointment.doctorName
        holder.appointmentDate.text = dateTimeParts[0]
        holder.appointmentTime.text = dateTimeParts[1]
        holder.description.text = currentAppointment.appointmentDescription

        // Todo-Note: this piece of code is to delete the appointment that has been booked before
        holder.btnCancelAppointment.setOnClickListener {
            DoctorApi.retrofitService.cancelAppointmentById(appointmentId).enqueue(object: Callback<Appointment?>{
                override fun onResponse(
                    call: Call<Appointment?>,
                    response: Response<Appointment?>
                ) {
                    Toast.makeText(holder.itemView.context, "Delete Successfully", Toast.LENGTH_SHORT).show()
                    // the removeAt method removes the
                    // appointment that its cancel button clicked using the
                    // position parameter (the index of the clicked appointment),
                    // then notifyDataSetChanged to update the recyclerView
                    appointments.removeAt(position)
                    notifyDataSetChanged()
                }

                override fun onFailure(call: Call<Appointment?>, t: Throwable) {
                    Toast.makeText(holder.itemView.context, "Delete Unsuccessful", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }
}