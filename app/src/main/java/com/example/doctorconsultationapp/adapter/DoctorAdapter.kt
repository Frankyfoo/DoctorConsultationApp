package com.example.doctorconsultationapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doctorconsultationapp.R
import com.example.doctorconsultationapp.model.Doctor
import com.example.doctorconsultationapp.ui.DoctorDetailActivity

class DoctorAdapter(private val doctors: List<Doctor>) :
    RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    inner class DoctorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val doctorImage: ImageView = itemView.findViewById(R.id.imgDoctor)
        val doctorName: TextView = itemView.findViewById(R.id.tvDoctorName)
        private val cardViewDoctor: CardView = itemView.findViewById(R.id.cdDoctor)

        init {
            cardViewDoctor.setOnClickListener {
                val context = itemView.context
                // Handle click on item
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Ensure that the position is valid
                    val doctorId = getDoctorId(position)
                    val intent = Intent(context, DoctorDetailActivity::class.java)
                    intent.putExtra("doctorId", doctorId)
                    context.startActivity(intent)
                }
            }
        }

        private fun getDoctorId(position: Int): String {
            return doctors[position].doctorId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.doctor_list_item, parent, false)
        return DoctorViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return doctors.size
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val currentDoctor = doctors[position]

        Glide.with(holder.itemView.context)
            .load(currentDoctor.imageURL)
            .into(holder.doctorImage)
        holder.doctorName.text = currentDoctor.doctorName
    }

}