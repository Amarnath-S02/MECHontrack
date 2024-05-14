package com.example.carserviceandroidapp

class ServiceHistoryItems {
    constructor()

    var customerName: String? = null
    var customerNumber: String? = null
    var customerEmail: String? = null
    var serviceCompletedDate: String? = null
    var servicePickupDate: String? = null
    var serviceDropOffDate: String? = null
    var serviceAppointmentStatus: String? = null
    var serviceDetails: String? = null
    var serviceAppointmentID: String? = null
    var serviceAppointmentType: String? = null

    // Constructor for "Completed Status"
    //    public ServiceHistoryItems(String customerName, String customerNumber, String customerEmail, String serviceCompletedDate, String servicePickupDate, String serviceDropOffDate, String serviceAppointmentStatus, String serviceDetails) {
    //        CustomerName = customerName;
    //        CustomerNumber = customerNumber;
    //        CustomerEmail = customerEmail;
    //        ServiceCompletedDate = serviceCompletedDate;
    //        ServicePickupDate = servicePickupDate;
    //        ServiceDropOffDate = serviceDropOffDate;
    //        ServiceAppointmentStatus = serviceAppointmentStatus;
    //        ServiceDetails = serviceDetails;
    //    }
    constructor(
        serviceAppointmentID: String?,
        customerName: String?,
        customerNumber: String?,
        customerEmail: String?,
        serviceCompletedDate: String?,
        servicePickupDate: String?,
        serviceDropOffDate: String?,
        serviceAppointmentStatus: String?,
        serviceDetails: String?,
        serviceAppointmentType: String?
    ) {
        this.serviceAppointmentID = serviceAppointmentID
        this.customerName = customerName
        this.customerNumber = customerNumber
        this.customerEmail = customerEmail
        this.serviceCompletedDate = serviceCompletedDate
        this.servicePickupDate = servicePickupDate
        this.serviceDropOffDate = serviceDropOffDate
        this.serviceAppointmentStatus = serviceAppointmentStatus
        this.serviceDetails = serviceDetails
        this.serviceAppointmentType = serviceAppointmentType
    }

    // Constructor for "Cancelled Status"
    //    public ServiceHistoryItems(String customerName, String customerNumber, String customerEmail, String serviceAppointmentStatus, String serviceDetails, String serviceDropOffDate) {
    //        CustomerName = customerName;
    //        CustomerNumber = customerNumber;
    //        CustomerEmail = customerEmail;
    //        ServiceAppointmentStatus = serviceAppointmentStatus;
    //        ServiceDetails = serviceDetails;
    //        ServiceDropOffDate = serviceDropOffDate;
    //    }
    constructor(
        serviceAppointmentID: String?,
        customerName: String?,
        customerNumber: String?,
        customerEmail: String?,
        serviceAppointmentStatus: String?,
        serviceDetails: String?,
        serviceDropOffDate: String?,
        serviceAppointmentType: String?
    ) {
        this.serviceAppointmentID = serviceAppointmentID
        this.customerName = customerName
        this.customerNumber = customerNumber
        this.customerEmail = customerEmail
        this.serviceAppointmentStatus = serviceAppointmentStatus
        this.serviceDetails = serviceDetails
        this.serviceDropOffDate = serviceDropOffDate
        this.serviceAppointmentType = serviceAppointmentType
    }
}
