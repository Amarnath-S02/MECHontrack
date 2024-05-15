package com.example.carserviceandroidapp

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ServiceHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var nameView: TextView
    var numberView: TextView
    var emailView: TextView
    var appointmentTypeView: TextView
    var appointmentStatusView: TextView
    var deliveryTypeView: TextView
    var idNumberView: TextView
    var completedDateView: TextView
    var pickupDateView: TextView
    var dropOffDateView: TextView
    var completed: TextView
    var pickup: TextView

    init {
        idNumberView = itemView.findViewById(R.id.iD_Number)
        nameView = itemView.findViewById(R.id.name)
        numberView = itemView.findViewById(R.id.number)
        emailView = itemView.findViewById(R.id.email)
        appointmentTypeView = itemView.findViewById(R.id.appointmentType)
        deliveryTypeView = itemView.findViewById(R.id.deliveryType)
        appointmentStatusView = itemView.findViewById(R.id.appointmentStatusHead)
        completedDateView = itemView.findViewById(R.id.completedDate)
        pickupDateView = itemView.findViewById(R.id.pickupDate)
        dropOffDateView = itemView.findViewById(R.id.dropOffDate)
        completed = itemView.findViewById(R.id.completed)
        pickup = itemView.findViewById(R.id.pickUpReady)
    }
}
