package com.example.carserviceandroidapp.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carserviceandroidapp.CustomerServiceHistoryItems
import com.example.carserviceandroidapp.CustomerServiceHistoryViewHolder
import com.example.carserviceandroidapp.R

class CustomerServiceHistoryAdapter(
    var context: Context?,
    private var customerServiceHistoryItems: List<CustomerServiceHistoryItems>
) : RecyclerView.Adapter<CustomerServiceHistoryViewHolder>() {
    public override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomerServiceHistoryViewHolder {
        return CustomerServiceHistoryViewHolder(
            (LayoutInflater.from(context)
                .inflate(R.layout.customerhistory_item_view, parent, false))
        )
    }

    override fun onBindViewHolder(
        holder: CustomerServiceHistoryViewHolder,
        position: Int
    ) {
        holder.tvhistappointmentIDInt.setText(
            customerServiceHistoryItems[position].histappointmentIDInt.toString()
        )
        holder.tvhistbookedServiceProviderName.setText(
            customerServiceHistoryItems[position].histbookedServiceProviderName
        )
        holder.tvhistserviceAvailed.setText(
            customerServiceHistoryItems[position].histserviceAvailed
        )
        holder.tvhistbookedServiceProviderAddress.setText(
            customerServiceHistoryItems[position].histbookedServiceProviderAddress
        )
        holder.tvhistbookingStatus.setText(
            customerServiceHistoryItems[position].histbookingStatus
        )
        holder.tvhistdropoffAppointmentDate.setText(
            customerServiceHistoryItems[position].histdropoffAppointmentDate
        )
        //holder.tvhistcustomDropOffTime.setText(customer_serviceHistory_items.get(position).getHistcustomDropOffTime());
        holder.tvhistpickupAppointmentDate.setText(
            customerServiceHistoryItems[position].histpickupAppointmentDate
        )
        // holder.tvhistcustomPickupTime.setText(customer_serviceHistory_items.get(position).getHistcustomPickupTime());
        holder.tvhistcustomAppointmentType.setText(
            customerServiceHistoryItems[position].histcustomAppType
        )
        if ((customerServiceHistoryItems[position].histbookingStatus == "Ongoing")) {
            val chosenColor: Int = Color.rgb(247, 201, 16)
            holder.tvhistbookingStatus.setTextColor(Color.WHITE)
            holder.tvhistdropoffAppointmentDate.setTextColor(chosenColor)
            holder.tvhistpickupAppointmentDate.setTextColor(chosenColor)
            val drawable: GradientDrawable = GradientDrawable()
            drawable.setShape(GradientDrawable.RECTANGLE)
            drawable.setCornerRadius(20f)
            drawable.setColor(chosenColor)
            holder.tvhistbookingStatus.setBackgroundDrawable(drawable)
        } else if ((customerServiceHistoryItems[position].histbookingStatus == "Cancelled")
        ) {
            holder.tvhistbookingStatus.setTextColor(Color.WHITE)
            holder.tvhistdropoffAppointmentDate.setTextColor(Color.RED)
            holder.tvhistpickupAppointmentDate.setTextColor(Color.RED)
            val drawable: GradientDrawable = GradientDrawable()
            drawable.setShape(GradientDrawable.RECTANGLE)
            drawable.setCornerRadius(20f)
            drawable.setColor(Color.RED)
            holder.tvhistbookingStatus.setBackgroundDrawable(drawable)
        } else if ((customerServiceHistoryItems[position].histbookingStatus == "Completed")) {
            val chosenColor: Int = Color.rgb(101, 207, 114)
            holder.tvhistdropoffAppointmentDate.setTextColor(chosenColor)
            holder.tvhistpickupAppointmentDate.setTextColor(chosenColor)
            holder.tvhistbookingStatus.setTextColor(Color.WHITE)
            val drawable: GradientDrawable = GradientDrawable()
            drawable.setShape(GradientDrawable.RECTANGLE)
            drawable.setCornerRadius(20f)
            drawable.setColor(chosenColor)
            holder.tvhistbookingStatus.setBackgroundDrawable(drawable)
        }
    }

    public override fun getItemCount(): Int {
        return customerServiceHistoryItems.size
    }
}
