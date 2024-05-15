package com.example.carserviceandroidapp

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomerAppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var tvhistappointmentIDLabel: TextView
    var tvhistappointmentIDInt: TextView
    var tvhistbookedServiceProviderName: TextView
    var tvhistserviceAvailed: TextView
    var tvhistbookedServiceProviderAddress: TextView
    var tvhistbookingStatus: TextView
    var tvhistdropofflabel: TextView
    var tvhistdropoffAppointmentDate: TextView
    var tvhistpickuplabel: TextView
    var tvhistpickupAppointmentDate: TextView
    var tvhistAppointmentType: TextView
    var relativeLayout: RelativeLayout

    init {
        tvhistappointmentIDLabel = itemView.findViewById(R.id.histappointmentIDLabel)
        tvhistappointmentIDInt = itemView.findViewById(R.id.histappointmentIDInt)
        tvhistbookedServiceProviderName = itemView.findViewById(R.id.histbookedServiceProviderName)
        tvhistserviceAvailed = itemView.findViewById(R.id.histserviceAvailed)
        tvhistbookedServiceProviderAddress =
            itemView.findViewById(R.id.histbookedServiceProviderAddress)
        tvhistbookingStatus = itemView.findViewById(R.id.histbookingStatus)
        tvhistdropofflabel = itemView.findViewById(R.id.histdropofflabel)
        tvhistdropoffAppointmentDate = itemView.findViewById(R.id.histdropoffAppointmentDate)
        tvhistpickuplabel = itemView.findViewById(R.id.histpickuplabel)
        tvhistpickupAppointmentDate = itemView.findViewById(R.id.histpickupAppointmentDate)
        tvhistAppointmentType = itemView.findViewById(R.id.histserviceAppointType)
        relativeLayout = itemView.findViewById(R.id.container_custappointment_item)
    }
}
