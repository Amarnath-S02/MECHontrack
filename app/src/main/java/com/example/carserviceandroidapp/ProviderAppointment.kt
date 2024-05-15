package com.example.carserviceandroidapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carserviceandroidapp.adapter.ProviderAppointmentAdapter

class ProviderAppointment : Fragment(), ProviderAppointmentInterface {
    var serviceDetailID = 0
    var appointmentID = 0
    var customerID = 0
    var appointmentStatus: String? = null
    var dropOffDateTime: String? = null
    var dropOffLocation: String? = null
    var pickUpDateTime: String? = null
    var pickUpLocation: String? = null
    var serviceType: String? = null
    var customerName: String? = null
    var customerAddress: String? = null
    var customerContact: String? = null
    var customerEmail: String? = null
    var selectedService: String? = null
    var serviceListID: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dbh = DBHelper(activity)
        val view = inflater.inflate(R.layout.activity_provider_appointment, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewProviderAppointment)
        val providerAppointmentClass: MutableList<ProviderAppointmentClass> = ArrayList()
        val providerAppointmentsOngoing: MutableList<ProviderAppointmentClass> = ArrayList()
        val customerApointmentItems: List<CustomerApointmentItems> = ArrayList()
        val customerAppointmentsOngoing: List<CustomerApointmentItems> = ArrayList()
        val spID = ServiceProvider.ServiceProviderID
        val cursorAppointment = dbh.appointment
        val cursorCustomer = dbh.customerInfo
        val cursorAppDetail = dbh.appointmentDetail
        val cursorServiceList = dbh.serviceList
        val cursorServiceDetail = dbh.serviceDetails
        try {
            if (cursorAppointment!!.count > 0) {
                while (cursorAppointment.moveToNext()) {
                    if (cursorAppointment.getInt(cursorAppointment.getColumnIndexOrThrow("ServiceProviderID")) == spID) {
                        appointmentID =
                            cursorAppointment.getInt(cursorAppointment.getColumnIndexOrThrow("AppointmentID"))
                        customerID =
                            cursorAppointment.getInt(cursorAppointment.getColumnIndexOrThrow("Userid"))
                        if (cursorCustomer!!.count > 0) {
                            cursorCustomer.moveToPosition(-1)
                            while (cursorCustomer.moveToNext()) {
                                if (cursorCustomer.getInt(cursorCustomer.getColumnIndexOrThrow("Userid")) == customerID) {
                                    customerName = cursorCustomer.getString(
                                        cursorCustomer.getColumnIndexOrThrow("name")
                                    )
                                    customerAddress = cursorCustomer.getString(
                                        cursorCustomer.getColumnIndexOrThrow("address")
                                    )
                                    customerContact = cursorCustomer.getString(
                                        cursorCustomer.getColumnIndexOrThrow("mobile")
                                    )
                                    customerEmail = cursorCustomer.getString(
                                        cursorCustomer.getColumnIndexOrThrow("email")
                                    )
                                }
                            }
                        }

//                            spAddress = cusAddress;
                        appointmentStatus =
                            cursorAppointment.getString(cursorAppointment.getColumnIndexOrThrow("AppointmentStatus"))
                        dropOffDateTime =
                            cursorAppointment.getString(cursorAppointment.getColumnIndexOrThrow("DropOffTimeDate"))
                        dropOffLocation =
                            cursorAppointment.getString(cursorAppointment.getColumnIndexOrThrow("DropOffLocation"))
                        pickUpDateTime =
                            cursorAppointment.getString(cursorAppointment.getColumnIndexOrThrow("PickUpDateTime"))
                        pickUpLocation =
                            cursorAppointment.getString(cursorAppointment.getColumnIndexOrThrow("PickUpLocation"))
                        serviceType =
                            cursorAppointment.getString(cursorAppointment.getColumnIndexOrThrow("AppointmentType"))
                        //
                        if (cursorAppDetail!!.count > 0) {
                            cursorAppDetail.moveToPosition(-1)
                            while (cursorAppDetail.moveToNext()) {
                                if (cursorAppDetail.getInt(cursorAppDetail.getColumnIndexOrThrow("AppointmentID")) == appointmentID) {
                                    serviceListID = cursorAppDetail.getString(
                                        cursorAppDetail.getColumnIndexOrThrow("ServiceListID")
                                    )
                                }
                            }
                        }
                        if (cursorServiceList!!.count > 0) {
                            cursorServiceList.moveToPosition(-1)
                            while (cursorServiceList.moveToNext()) {
                                if (cursorServiceList.getString(
                                        cursorServiceList.getColumnIndexOrThrow(
                                            "ServiceListID"
                                        )
                                    ) == serviceListID
                                ) {
                                    serviceDetailID = cursorServiceList.getInt(
                                        cursorServiceList.getColumnIndexOrThrow("ServiceDetailID")
                                    )
                                }
                            }
                        }
                        if (cursorServiceDetail!!.count > 0) {
                            cursorServiceDetail.moveToPosition(-1)
                            while (cursorServiceDetail.moveToNext()) {
                                if (cursorServiceDetail.getInt(
                                        cursorServiceDetail.getColumnIndexOrThrow(
                                            "ServiceDetailID"
                                        )
                                    ) == serviceDetailID
                                ) {
                                    selectedService = cursorServiceDetail.getString(
                                        cursorServiceDetail.getColumnIndexOrThrow("ServiceName")
                                    )
                                }
                            }
                        }
                        providerAppointmentClass.add(
                            ProviderAppointmentClass(
                                appointmentID,
                                customerName,
                                customerContact,
                                customerEmail,
                                selectedService,
                                customerAddress,
                                appointmentStatus,
                                dropOffDateTime,
                                pickUpDateTime,
                                pickUpLocation,
                                dropOffLocation,
                                serviceType
                            )
                        )
                    }
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        for (ongoingItem in providerAppointmentClass) {
            if (ongoingItem.appointmentStatus == "Ongoing") {
                providerAppointmentsOngoing.add(ongoingItem)
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter =
            ProviderAppointmentAdapter(activity, providerAppointmentsOngoing, this)
        return view
    }

    override fun onItemClick(providerAppointmentClass: ProviderAppointmentClass) {
        val intent = Intent(activity, ProviderEditAppointment::class.java)
        intent.putExtra("AppointmentID", providerAppointmentClass.appointmentID)
        intent.putExtra("CustomerName", providerAppointmentClass.customerName)
        intent.putExtra("CustomerAddress", providerAppointmentClass.customerAddress)
        intent.putExtra("AppointmentStatus", providerAppointmentClass.appointmentStatus)
        intent.putExtra("DropOffDateTime", providerAppointmentClass.dropOffDateTime)
        intent.putExtra("PickupDateTime", providerAppointmentClass.pickUpDateTime)
        intent.putExtra("DropOffLocation", providerAppointmentClass.dropOffLocation)
        intent.putExtra("PickUpLocation", providerAppointmentClass.pickUpLocation)
        intent.putExtra("SelectedService", providerAppointmentClass.selectedService)
        intent.putExtra("CustomerContact", providerAppointmentClass.customerContact)
        intent.putExtra("CustomerEmail", providerAppointmentClass.customerEmail)
        intent.putExtra("AppointmentType", providerAppointmentClass.appointmentType)
        startActivity(intent)
    }
}