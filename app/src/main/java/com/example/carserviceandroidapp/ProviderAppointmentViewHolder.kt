package com.example.carserviceandroidapp

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProviderAppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var label: TextView
    var appointmentID: TextView
    var customerName: TextView
    var selectedServices: TextView
    var customerAddress: TextView
    var appointmentStatusHead: TextView
    var dropOffDateLabel: TextView
    var dropOffDate: TextView
    var pickUpDateLabel: TextView
    var pickUpDate: TextView
    var relativeLayout: RelativeLayout

    init {
        label = itemView.findViewById(R.id.label)
        appointmentID = itemView.findViewById(R.id.appointmentID)
        customerName = itemView.findViewById(R.id.customerName)
        selectedServices = itemView.findViewById(R.id.selectedServices)
        customerAddress = itemView.findViewById(R.id.customerAddress)
        appointmentStatusHead = itemView.findViewById(R.id.appointmentStatusHead)
        dropOffDateLabel = itemView.findViewById(R.id.dropOffDateLabel)
        dropOffDate = itemView.findViewById(R.id.dropOffDate)
        pickUpDateLabel = itemView.findViewById(R.id.pickUpDateLabel)
        pickUpDate = itemView.findViewById(R.id.pickUpDate)
        relativeLayout = itemView.findViewById(R.id.containerProviderAppointment)
    }
}
