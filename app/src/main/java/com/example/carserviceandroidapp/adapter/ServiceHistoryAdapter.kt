package com.example.carserviceandroidapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carserviceandroidapp.CustomerAppointmentsViewSelectInterface
import com.example.carserviceandroidapp.R
import com.example.carserviceandroidapp.ServiceHistoryItems
import com.example.carserviceandroidapp.ServiceHistoryViewHolder

class ServiceHistoryAdapter(var context: Context?, var items: List<ServiceHistoryItems>) :
    RecyclerView.Adapter<ServiceHistoryViewHolder>() {
    private val selectInterface: CustomerAppointmentsViewSelectInterface? = null

    //    public ServiceHistoryAdapter(Context applicationContext) {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceHistoryViewHolder {
        return ServiceHistoryViewHolder(
            LayoutInflater.from(context).inflate(R.layout.serivce_history_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ServiceHistoryViewHolder, position: Int) {
        val currentItem = items[position]
        holder.nameView.text = currentItem.customerName
        holder.numberView.text = currentItem.customerNumber
        holder.emailView.text = currentItem.customerEmail
        holder.appointmentTypeView.text = currentItem.serviceDetails
        holder.deliveryTypeView.text = currentItem.serviceAppointmentType
        holder.idNumberView.text = currentItem.serviceAppointmentID
        val appointmentStatus = currentItem.serviceAppointmentStatus
        holder.appointmentStatusView.text = appointmentStatus
        //Shows all parameters when "completed"
        if (appointmentStatus == "Completed") {
            holder.completedDateView.text = currentItem.serviceCompletedDate
            holder.pickupDateView.text = currentItem.servicePickupDate
            holder.dropOffDateView.text = currentItem.serviceDropOffDate
            holder.appointmentStatusView.setBackgroundResource(R.drawable.green_rounded_rectangle)
        } else if (appointmentStatus == "Cancelled") {
            holder.pickupDateView.visibility = View.GONE
            holder.completedDateView.visibility = View.GONE
            holder.completed.visibility = View.GONE
            holder.pickup.visibility = View.GONE
            holder.dropOffDateView.text = currentItem.serviceDropOffDate
            holder.dropOffDateView.setTextColor(context!!.resources.getColor(R.color.red))
            holder.appointmentStatusView.setBackgroundResource(R.drawable.red_rounded_rectangle)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
