package com.example.carserviceandroidapp.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carserviceandroidapp.CustomerApointmentItems
import com.example.carserviceandroidapp.CustomerAppointmentViewHolder
import com.example.carserviceandroidapp.CustomerAppointmentsViewSelectInterface
import com.example.carserviceandroidapp.R

class CustomerAppointmentAdapter(
    var context: Context?,
    var customerApointmentItemsList: List<CustomerApointmentItems>,
    private val selectInterface: CustomerAppointmentsViewSelectInterface
) : RecyclerView.Adapter<CustomerAppointmentViewHolder>() {
    public override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomerAppointmentViewHolder {
        return CustomerAppointmentViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.customer_appointmentsview_item, parent, false)
        )
    }

    public override fun onBindViewHolder(holder: CustomerAppointmentViewHolder, position: Int) {
        holder.tvhistappointmentIDInt.setText(
            customerApointmentItemsList[position].histappointmentIDInt.toString()
        )
        holder.tvhistbookedServiceProviderName.setText(
            customerApointmentItemsList[position].histbookedServiceProviderName
        )
        holder.tvhistserviceAvailed.setText(
            customerApointmentItemsList[position].histserviceAvailed
        )
        holder.tvhistbookedServiceProviderAddress.setText(
            customerApointmentItemsList[position].histbookedServiceProviderAddress
        )
        holder.tvhistbookingStatus.setText(
            customerApointmentItemsList[position].histbookingStatus
        )
        holder.tvhistdropoffAppointmentDate.setText(
            customerApointmentItemsList[position].histdropoffAppointmentDate
        )
        //holder.tvhistcustomDropOffTime.setText(customerApointmentItemsList.get(position).getHistcustomDropOffTime());
        holder.tvhistpickupAppointmentDate.setText(
            customerApointmentItemsList[position].histpickupAppointmentDate
        )
        //holder.tvhistcustomPickupTime.setText(customerApointmentItemsList.get(position).getHistcustomPickupTime());
        holder.tvhistAppointmentType.setText(
            customerApointmentItemsList[position].histAppointType
        )
        if ((customerApointmentItemsList[position].histbookingStatus == "Ongoing")) {
            val chosenColor: Int = Color.rgb(247, 201, 16)
            holder.tvhistbookingStatus.setTextColor(Color.WHITE)
            holder.tvhistdropoffAppointmentDate.setTextColor(chosenColor)
            holder.tvhistpickupAppointmentDate.setTextColor(chosenColor)
            val drawable: GradientDrawable = GradientDrawable()
            drawable.setShape(GradientDrawable.RECTANGLE)
            drawable.setCornerRadius(20f)
            drawable.setColor(chosenColor)
            holder.tvhistbookingStatus.setBackgroundDrawable(drawable)
        } else if ((customerApointmentItemsList[position].histbookingStatus == "Completed")) {
            val chosenColor: Int = Color.rgb(101, 207, 114)
            holder.tvhistdropoffAppointmentDate.setTextColor(chosenColor)
            holder.tvhistpickupAppointmentDate.setTextColor(chosenColor)
            holder.tvhistbookingStatus.setTextColor(Color.WHITE)
            val drawable: GradientDrawable = GradientDrawable()
            drawable.setShape(GradientDrawable.RECTANGLE)
            drawable.setCornerRadius(20f)
            drawable.setColor(chosenColor)
            holder.tvhistbookingStatus.setBackgroundDrawable(drawable)
        } else if ((customerApointmentItemsList[position].histbookingStatus == "Cancelled")) {
            holder.tvhistbookingStatus.setTextColor(Color.WHITE)
            holder.tvhistdropoffAppointmentDate.setTextColor(Color.RED)
            holder.tvhistpickupAppointmentDate.setTextColor(Color.RED)
            val drawable: GradientDrawable = GradientDrawable()
            drawable.setShape(GradientDrawable.RECTANGLE)
            drawable.setCornerRadius(20f)
            drawable.setColor(Color.RED)
            holder.tvhistbookingStatus.setBackgroundDrawable(drawable)
        }
        holder.relativeLayout.setOnClickListener {
            selectInterface.onItemClick(
                customerApointmentItemsList[holder.getAdapterPosition()]
            )
        }
    }

    public override fun getItemCount(): Int {
        return customerApointmentItemsList.size
    }
}
