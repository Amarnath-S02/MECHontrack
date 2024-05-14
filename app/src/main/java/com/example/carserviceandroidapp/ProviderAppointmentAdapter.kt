package com.example.carserviceandroidapp

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ProviderAppointmentAdapter(
    var context: Context?,
    var providerAppointmentClass: List<ProviderAppointmentClass>,
    private val providerAppointmentInterface: ProviderAppointmentInterface
) : RecyclerView.Adapter<ProviderAppointmentViewHolder>() {
    public override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProviderAppointmentViewHolder {
        return ProviderAppointmentViewHolder(
            LayoutInflater.from(context).inflate(R.layout.provider_appointmentview, parent, false)
        )
    }

    public override fun onBindViewHolder(holder: ProviderAppointmentViewHolder, position: Int) {
        holder.appointmentID.setText(
            providerAppointmentClass[position].appointmentID.toString()
        )
        holder.customerName.setText(providerAppointmentClass[position].customerName)
        holder.selectedServices.setText(providerAppointmentClass[position].selectedService)
        holder.customerAddress.setText(providerAppointmentClass[position].customerAddress)
        holder.appointmentStatusHead.setText(
            providerAppointmentClass[position].appointmentStatus
        )
        holder.dropOffDate.setText(providerAppointmentClass[position].dropOffDateTime)
        holder.pickUpDate.setText(providerAppointmentClass[position].pickUpDateTime)
        if ((providerAppointmentClass[position].appointmentStatus == "Ongoing")) {
            val chosenColor: Int = Color.rgb(247, 201, 16)
            holder.appointmentStatusHead.setTextColor(Color.WHITE)
            holder.dropOffDate.setTextColor(chosenColor)
            holder.pickUpDate.setTextColor(chosenColor)
            val drawable: GradientDrawable = GradientDrawable()
            drawable.setShape(GradientDrawable.RECTANGLE)
            drawable.setCornerRadius(20f)
            drawable.setColor(chosenColor)
            holder.appointmentStatusHead.setBackgroundDrawable(drawable)
        } else if ((providerAppointmentClass[position].appointmentStatus == "Completed")) {
            val chosenColor: Int = Color.rgb(101, 207, 114)
            holder.dropOffDate.setTextColor(chosenColor)
            holder.pickUpDate.setTextColor(chosenColor)
            holder.appointmentStatusHead.setTextColor(Color.WHITE)
            val drawable: GradientDrawable = GradientDrawable()
            drawable.setShape(GradientDrawable.RECTANGLE)
            drawable.setCornerRadius(20f)
            drawable.setColor(chosenColor)
            holder.appointmentStatusHead.setBackgroundDrawable(drawable)
        } else if ((providerAppointmentClass[position].appointmentStatus == "Cancelled")) {
            holder.appointmentStatusHead.setTextColor(Color.WHITE)
            holder.dropOffDate.setTextColor(Color.RED)
            holder.pickUpDate.setTextColor(Color.RED)
            val drawable: GradientDrawable = GradientDrawable()
            drawable.setShape(GradientDrawable.RECTANGLE)
            drawable.setCornerRadius(20f)
            drawable.setColor(Color.RED)
            holder.appointmentStatusHead.setBackgroundDrawable(drawable)
        }
        holder.relativeLayout.setOnClickListener {
            providerAppointmentInterface.onItemClick(
                providerAppointmentClass.get(holder.getAdapterPosition())
            )
        }
    }

    public override fun getItemCount(): Int {
        return providerAppointmentClass.size
    }
}
