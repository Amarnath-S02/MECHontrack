package com.example.carserviceandroidapp

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carserviceandroidapp.adapter.CustomerAppointmentAdapter

class CustomerAppointmentsView() : Fragment(), CustomerAppointmentsViewSelectInterface {
    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dbh: DBHelper = DBHelper(getActivity())
        val view: View =
            inflater.inflate(R.layout.activity_customer_appointments_view, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerviewCustViewAppointments)
        val customerApointmentItems: MutableList<CustomerApointmentItems> = ArrayList()
        val customerAppointmentsOngoing: MutableList<CustomerApointmentItems> = ArrayList()
        val userId: Int = Customer.CustomerID
        val cursorAppointment: Cursor? = dbh.appointment
        val cursorServiceProvider: Cursor? = dbh.serviceProviderDataAll
        val cursorAppDetail: Cursor? = dbh.appointmentDetail
        val cursorServiceList: Cursor? = dbh.serviceList
        val cursorServiceDetail: Cursor? = dbh.serviceDetails
        var spName: String = ""
        var spStreet: String = ""
        var spCity: String = ""
        var spProvince: String = ""
        var spPostal: String = ""
        var spPhone: String = ""
        var spEmail: String = ""
        var serviceAvailed: String = ""
        var serviceListID: String = ""
        var serviceDetailID: Int = 0
        var appID: Int = 0
        var appSPID: Int = -1
        try {
            if (cursorAppointment!!.getCount() > 0) {
                while (cursorAppointment.moveToNext()) {
                    if (cursorAppointment.getInt(cursorAppointment.getColumnIndexOrThrow("Userid")) == userId) {
                        appID =
                            cursorAppointment.getInt(cursorAppointment.getColumnIndexOrThrow("AppointmentID"))
                        appSPID =
                            cursorAppointment.getInt(cursorAppointment.getColumnIndexOrThrow("ServiceProviderID"))
                        if (cursorServiceProvider!!.getCount() > 0) {
                            cursorServiceProvider.moveToPosition(-1)
                            while (cursorServiceProvider.moveToNext()) {
                                if (cursorServiceProvider.getInt(
                                        cursorServiceProvider.getColumnIndexOrThrow(
                                            "ServiceProviderID"
                                        )
                                    ) == appSPID
                                ) {
                                    spName = cursorServiceProvider.getString(
                                        cursorServiceProvider.getColumnIndexOrThrow("serviceProviderFullName")
                                    )
                                    spStreet = cursorServiceProvider.getString(
                                        cursorServiceProvider.getColumnIndexOrThrow("street")
                                    )
                                    spCity = cursorServiceProvider.getString(
                                        cursorServiceProvider.getColumnIndexOrThrow("city")
                                    )
                                    spProvince = cursorServiceProvider.getString(
                                        cursorServiceProvider.getColumnIndexOrThrow("province")
                                    )
                                    spPostal = cursorServiceProvider.getString(
                                        cursorServiceProvider.getColumnIndexOrThrow("postalCode")
                                    )
                                    spPhone = cursorServiceProvider.getString(
                                        cursorServiceProvider.getColumnIndexOrThrow("phone")
                                    )
                                    spEmail = cursorServiceProvider.getString(
                                        cursorServiceProvider.getColumnIndexOrThrow("email")
                                    )
                                }
                            }
                        }
                        val spAddress: String =
                            spStreet + ", " + spCity + ", " + spProvince + " " + spPostal
                        val appStatus: String = cursorAppointment.getString(
                            cursorAppointment.getColumnIndexOrThrow("AppointmentStatus")
                        )
                        val dropOffDT: String = cursorAppointment.getString(
                            cursorAppointment.getColumnIndexOrThrow("DropOffTimeDate")
                        )
                        val dropOffLoc: String = cursorAppointment.getString(
                            cursorAppointment.getColumnIndexOrThrow("DropOffLocation")
                        )
                        val pickupDT: String = cursorAppointment.getString(
                            cursorAppointment.getColumnIndexOrThrow("PickUpDateTime")
                        )
                        val pickupLoc: String = cursorAppointment.getString(
                            cursorAppointment.getColumnIndexOrThrow("PickUpLocation")
                        )
                        val appType: String = cursorAppointment.getString(
                            cursorAppointment.getColumnIndexOrThrow("AppointmentType")
                        )
                        //
                        if (cursorAppDetail!!.getCount() > 0) {
                            cursorAppDetail.moveToPosition(-1)
                            while (cursorAppDetail.moveToNext()) {
                                if (cursorAppDetail.getInt(cursorAppDetail.getColumnIndexOrThrow("AppointmentID")) == appID) {
                                    serviceListID = cursorAppDetail.getString(
                                        cursorAppDetail.getColumnIndexOrThrow("ServiceListID")
                                    )
                                }
                            }
                        }
                        if (cursorServiceList!!.getCount() > 0) {
                            cursorServiceList.moveToPosition(-1)
                            while (cursorServiceList.moveToNext()) {
                                if ((cursorServiceList.getString(
                                        cursorServiceList.getColumnIndexOrThrow(
                                            "ServiceListID"
                                        )
                                    ) == serviceListID)
                                ) {
                                    serviceDetailID = cursorServiceList.getInt(
                                        cursorServiceList.getColumnIndexOrThrow("ServiceDetailID")
                                    )
                                }
                            }
                        }
                        if (cursorServiceDetail!!.getCount() > 0) {
                            cursorServiceDetail.moveToPosition(-1)
                            while (cursorServiceDetail.moveToNext()) {
                                if (cursorServiceDetail.getInt(
                                        cursorServiceDetail.getColumnIndexOrThrow(
                                            "ServiceDetailID"
                                        )
                                    ) == serviceDetailID
                                ) {
                                    serviceAvailed = cursorServiceDetail.getString(
                                        cursorServiceDetail.getColumnIndexOrThrow("ServiceName")
                                    )
                                }
                            }
                        }
                        customerApointmentItems.add(
                            CustomerApointmentItems(
                                appID,
                                spName,
                                serviceAvailed,
                                spAddress,
                                appStatus,
                                dropOffDT,
                                "",
                                pickupDT,
                                "",
                                dropOffLoc,
                                pickupLoc,
                                spPhone,
                                spEmail,
                                appType
                            )
                        )
                    }
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        for (ongoingItem: CustomerApointmentItems in customerApointmentItems) {
            if ((ongoingItem.histbookingStatus == "Ongoing")) {
                customerAppointmentsOngoing.add(ongoingItem)
            }
        }
        recyclerView.setLayoutManager(LinearLayoutManager(getActivity()))
        recyclerView.setAdapter(
            CustomerAppointmentAdapter(
                getActivity(),
                customerAppointmentsOngoing,
                this
            )
        )
        return view
    }

    public override fun onItemClick(customerApointmentItems: CustomerApointmentItems) {
        val intent: Intent = Intent(getActivity(), CustomerEditAppointment::class.java)
        intent.putExtra("AppId", customerApointmentItems.histappointmentIDInt)
        intent.putExtra(
            "ServiceProviderName",
            customerApointmentItems.histbookedServiceProviderName
        )
        intent.putExtra("SPAddress", customerApointmentItems.histbookedServiceProviderAddress)
        intent.putExtra("AppStatus", customerApointmentItems.histbookingStatus)
        intent.putExtra("DropoffD", customerApointmentItems.histdropoffAppointmentDate)
        intent.putExtra("DropoffT", customerApointmentItems.histcustomDropOffTime)
        intent.putExtra("PickupD", customerApointmentItems.histpickupAppointmentDate)
        intent.putExtra("PickupT", customerApointmentItems.histcustomPickupTime)
        intent.putExtra("DropoffLoc", customerApointmentItems.histcustomDropOffLoc)
        intent.putExtra("PickupLoc", customerApointmentItems.histcustomPickupLoc)
        intent.putExtra("ServiceDet", customerApointmentItems.histserviceAvailed)
        intent.putExtra("SPPhone", customerApointmentItems.histSPPhone)
        intent.putExtra("SPEmail", customerApointmentItems.histSPEmail)
        intent.putExtra("AppType", customerApointmentItems.histAppointType)
        //place cell number here
        //place email address
        startActivity(intent)
    } //  public abstract View onCreate(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
}