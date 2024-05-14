package com.example.carserviceandroidapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context?) : SQLiteOpenHelper(context, "Userdata.db", null, 1) {
    override fun onCreate(DB: SQLiteDatabase) {
        // This method is only called if the database does not exist
        // You can create the database schema here if needed

        // Create the Customer table
        DB.execSQL(
            "create table IF NOT EXISTS CUSTOMER(Userid INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT, password TEXT, " +
                "email TEXT, mobile TEXT , address TEXT)"
        )

        // Create the Appointment table
        DB.execSQL(
            "CREATE TABLE IF NOT EXISTS APPOINTMENT (AppointmentID INTEGER PRIMARY KEY AUTOINCREMENT, Userid INTEGER, ServiceProviderID INTEGER, " +
                "PickUpDateTime DATE, PickUpLocation TEXT , PickUpReadyDate DATE, " +
                "DropOffTimeDate DATE, DropOffLocation TEXT, BookingDate DATE, CancelledDate DATE , AppointmentType TEXT, " +
                "AppointmentStatus TEXT, FOREIGN KEY(Userid) REFERENCES CUSTOMER(Userid), " +
                "FOREIGN KEY(ServiceProviderID) REFERENCES SERVICE_PROVIDER(ServiceProviderID))"
        )

        // Create the Service Provider table
        DB.execSQL(
            "CREATE TABLE IF NOT EXISTS SERVICE_PROVIDER (ServiceProviderID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "serviceProviderPassword TEXT, serviceProviderFullName TEXT, street TEXT, city TEXT, province TEXT, " +
                "postalCode TEXT, phone TEXT, email TEXT, imageName TEXT)"
        )

        // Create the Appointment Detail table
        DB.execSQL(
            "CREATE TABLE IF NOT EXISTS APPOINTMENT_DETAIL(AppointmentDetailID INTEGER PRIMARY KEY AUTOINCREMENT,AppointmentID INTEGER, ServiceListID TEXT, " +
                "FOREIGN KEY(AppointmentID) REFERENCES APPOINTMENT(AppointmentID), FOREIGN KEY(ServiceListID) REFERENCES SERVICE_LIST(ServiceListID) )"
        )

        // Create the Service List provided by each Service Provider table
        DB.execSQL(
            "CREATE TABLE IF NOT EXISTS SERVICE_LIST(ServiceListID TEXT, ServiceProviderID INTEGER, " +
                "ServiceDetailID INTEGER, FOREIGN KEY(ServiceProviderID) REFERENCES SERVICE_PROVIDER(ServiceProviderID), " +
                "FOREIGN KEY(ServiceDetailID) REFERENCES SERVICE_DETAIL(ServiceDetailID) )"
        )


        // Create the Service Detail table
        DB.execSQL(
            "CREATE TABLE IF NOT EXISTS SERVICE_DETAIL(ServiceDetailID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ServiceName TEXT, ServiceInformation TEXT)"
        )
    }

    override fun onUpgrade(DB: SQLiteDatabase, i: Int, ii: Int) {
        DB.execSQL("drop Table if exists Customer")
    }

    fun insertServiceList(
        serviceListID: String?,
        serviceProviderID: Int,
        serviceDetailID: Int
    ): Boolean {
        val DB = this.writableDatabase
        val Values = ContentValues()
        Values.put("ServiceListID", serviceListID)
        Values.put("ServiceProviderID", serviceProviderID)
        Values.put("ServiceDetailID", serviceDetailID)
        val result = DB.insert("SERVICE_LIST", null, Values)
        return if (result == -1L) {
            false
        } else {
            true
        }
    }

    fun insertAppointmentDetail(AppointmentID: Int, ServiceListID: String?): Boolean {
        val DB = this.writableDatabase
        val Values = ContentValues()
        Values.put("AppointmentID", AppointmentID)
        Values.put("ServiceListID", ServiceListID)
        val result = DB.insert("APPOINTMENT_DETAIL", null, Values)
        return if (result == -1L) {
            false
        } else {
            true
        }
    }

    fun insertServiceDetail(serviceName: String?, serviceInformation: String?): Boolean {
        val DB = this.writableDatabase
        val Values = ContentValues()
        Values.put("ServiceName", serviceName)
        Values.put("ServiceInformation", serviceInformation)
        val result = DB.insert("SERVICE_DETAIL", null, Values)
        return if (result == -1L) {
            false
        } else {
            true
        }
    }

    fun insertuserdata(
        name: String?,
        password: String?,
        email: String?,
        mobile: String?,
        address: String?
    ): Boolean {
        val DB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("password", password)
        contentValues.put("email", email)
        contentValues.put("mobile", mobile)
        contentValues.put("address", address)
        val result = DB.insert("CUSTOMER", null, contentValues)
        return if (result == -1L) {
            false
        } else {
            true
        }
    }

    fun insertServiceProvider(
        password: String?, fullName: String?, street: String?, city: String?,
        province: String?, postalCode: String?, email: String?, phone: String?, imageName: String?
    ): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("serviceProviderPassword", password)
        values.put("serviceProviderFullName", fullName)
        values.put("street", street)
        values.put("city", city)
        values.put("province", province)
        values.put("postalCode", postalCode)
        values.put("email", email)
        values.put("phone", phone)
        values.put("imageName", imageName)
        val result = db.insert("SERVICE_PROVIDER", null, values)
        return if (result == -1L) {
            false
        } else {
            true
        }
    }

    fun insertAppointment(
        userID: Int,
        serviceProviderID: Int,
        pickupDateTime: String?,
        pickupLocation: String?,
        pickupReadyDate: String?,
        DropoffTimeDate: String?,
        DropoffLocation: String?,
        BookingDate: String?,
        CancelledDate: String?,
        appointmentType: String?,
        AppointmentStatus: String?
    ): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Userid", userID)
        values.put("PickUpDateTime", pickupDateTime)
        values.put("ServiceProviderID", serviceProviderID)
        values.put("PickUpLocation", pickupLocation)
        values.put("PickUpReadyDate", pickupReadyDate)
        values.put("DropOffTimeDate", DropoffTimeDate)
        values.put("DropOffLocation", DropoffLocation)
        values.put("BookingDate", BookingDate)
        values.put("CancelledDate", CancelledDate)
        values.put("AppointmentType", appointmentType)
        values.put("AppointmentStatus", AppointmentStatus)
        val result = db.insert("APPOINTMENT", null, values)
        return if (result == -1L) {
            false
        } else {
            true
        }
    }

    fun updateData(
        name: String?,
        userID: Int,
        password: String?,
        email: String?,
        mobile: String?,
        address: String?
    ): Boolean {
        val db = this.writableDatabase
        // get a reference to the database
        // define the new values for the record
        val values = ContentValues()
        values.put("name", name)
        values.put("password", password)
        values.put("email", email)
        values.put("mobile", mobile)
        values.put("address", address)

        // define the selection criteria
        val selection = "userID = ?"
        val selectionArgs = arrayOf(Integer.toString(userID))
        val count = db.update("CUSTOMER", values, selection, selectionArgs)

        // check if the record was updated successfully
        return if (count > 0) {
            true

            // record updated successfully
        } else {
            false
            // failed to update record
        }
    }

    fun cancelApt(userID: Int, aptStatus: String?, cancelDate: String?): Boolean {
        val db = this.writableDatabase
        // get a reference to the database
        // define the new values for the record
        val values = ContentValues()
        values.put("AppointmentStatus", aptStatus)
        values.put("CancelledDate", cancelDate)


        // define the selection criteria
        val selection = "userID = ? AND AppointmentStatus != 'Cancelled'"
        val selectionArgs = arrayOf(Integer.toString(userID))
        val count = db.update("APPOINTMENT", values, selection, selectionArgs)

        // check if the record was updated successfully
        return if (count > 0) {
            true

            // record updated successfully
        } else {
            false
            // failed to update record
        }
    }

    fun deleteData(userID: Int): Boolean {
        val DB = this.writableDatabase
        // Define the WHERE clause (i.e., the condition that must be met for the record to be deleted)
        val selection = "userID = ?"
        val selectionArgs =
            arrayOf(Integer.toString(userID)) // Replace with the actual ID of the record to delete

        // Perform the deletion
        val deletedRows = DB.delete("CUSTOMER", selection, selectionArgs)

        // Check if the deletion was successful
        return if (deletedRows > 0) {
            true
            // Record was deleted successfully
        } else {
            false
            // Record was not found or could not be deleted
        }
    }

    val customerData: Cursor
        get() {
            val DB = this.writableDatabase
            return DB.rawQuery("Select * from CUSTOMER", null)
        }
    val serviceProviderInfo: Cursor
        //Get Service Provider Data
        get() {
            val DB = this.writableDatabase
            return DB.rawQuery("Select * from SERVICE_PROVIDER", null)
        }
    val appointmentID: Cursor
        get() {
            val DB = this.writableDatabase
            return DB.rawQuery("SELECT last_insert_rowid()", null)
        }
    val serviceProviderData: Cursor
        get() {
            val DB = this.writableDatabase
            return DB.rawQuery("Select distinct city from SERVICE_PROVIDER", null)
        }
    val serviceProviderList: Cursor
        get() {
            val DB = this.writableDatabase
            return DB.rawQuery("Select * from SERVICE_PROVIDER", null)
        }
    val serviceDetails: Cursor
        get() {
            val DB = this.writableDatabase
            return DB.rawQuery(
                "SELECT SD.ServiceName, SD.ServiceInformation, SL.ServiceProviderID, SD.ServiceDetailID \n" +
                    "FROM SERVICE_DETAIL SD\n" +
                    "INNER JOIN SERVICE_LIST SL ON SD.ServiceDetailID = SL.ServiceDetailID\n" +
                    "INNER JOIN SERVICE_PROVIDER SP ON SL.ServiceProviderID = SP.ServiceProviderID",
                null
            )
        }
    val appointment: Cursor
        get() {
            val DB = this.writableDatabase
            return DB.rawQuery("Select * from APPOINTMENT", null)
        }
    val dropOffLocationCust: Cursor
        get() {
            val DB = this.writableDatabase
            return DB.rawQuery("Select Userid,address from CUSTOMER", null)
        }
    val dropOffLocationSP: Cursor
        get() {
            val DB = this.writableDatabase
            return DB.rawQuery(
                "Select ServiceProviderID, street, city, province, postalCode from SERVICE_PROVIDER",
                null
            )
        }
    val serviceList: Cursor
        get() {
            val DB = this.writableDatabase
            return DB.rawQuery("Select * from SERVICE_LIST", null)
        }

    fun checkLogin(email: String, password: String): String {
        val db = this.readableDatabase
        val customerCursor = db.rawQuery(
            "SELECT * FROM CUSTOMER WHERE email=? AND password=?",
            arrayOf(email, password)
        )
        val providerCursor = db.rawQuery(
            "SELECT * FROM SERVICE_PROVIDER WHERE Email=? AND ServiceProviderPassword=?",
            arrayOf(email, password)
        )
        return if (customerCursor.count > 0) {
            "CUSTOMER"
        } else if (providerCursor.count > 0) {
            "SERVICE_PROVIDER"
        } else {
            "NOT_FOUND"
        }
    }

    fun checkLoginID(email: String, password: String): Array<String> {
        val db = this.readableDatabase
        val customerCursor = db.rawQuery(
            "SELECT * FROM CUSTOMER WHERE email=? AND password=?",
            arrayOf(email, password)
        )
        val providerCursor = db.rawQuery(
            "SELECT * FROM SERVICE_PROVIDER WHERE Email=? AND ServiceProviderPassword=?",
            arrayOf(email, password)
        )
        return if (customerCursor.count > 0) {
            customerCursor.moveToFirst()
            @SuppressLint("Range") val customerId =
                customerCursor.getInt(customerCursor.getColumnIndex("Userid"))
            arrayOf("CUSTOMER", customerId.toString())
        } else if (providerCursor.count > 0) {
            providerCursor.moveToFirst()
            @SuppressLint("Range") val serviceProviderId =
                providerCursor.getInt(providerCursor.getColumnIndex("ServiceProviderID"))
            arrayOf("SERVICE_PROVIDER", serviceProviderId.toString())
        } else {
            arrayOf("NOT_FOUND")
        }
    }

    fun checkEmail(email: String): Array<String> {
        val db = this.readableDatabase
        val customerCursor = db.rawQuery("SELECT * FROM CUSTOMER WHERE email=?", arrayOf(email))
        val providerCursor =
            db.rawQuery("SELECT * FROM SERVICE_PROVIDER WHERE Email=?", arrayOf(email))
        return if (customerCursor.count > 0) {
            customerCursor.moveToFirst()
            @SuppressLint("Range") val customerId =
                customerCursor.getInt(customerCursor.getColumnIndex("Userid"))
            @SuppressLint("Range") val password =
                customerCursor.getString(customerCursor.getColumnIndex("password"))
            arrayOf("CUSTOMER", customerId.toString(), password)
        } else if (providerCursor.count > 0) {
            providerCursor.moveToFirst()
            @SuppressLint("Range") val serviceProviderId =
                providerCursor.getInt(providerCursor.getColumnIndex("ServiceProviderID"))
            @SuppressLint("Range") val password =
                customerCursor.getString(customerCursor.getColumnIndex("serviceProviderPassword"))
            arrayOf("SERVICE_PROVIDER", serviceProviderId.toString(), password)
        } else {
            arrayOf("NOT_FOUND")
        }
    }

    val appointmentDetail: Cursor
        get() {
            val DB = this.writableDatabase
            return DB.rawQuery("Select * from APPOINTMENT_DETAIL", null)
        }
    val serviceProviderDataAll: Cursor
        get() {
            val DB = this.writableDatabase
            return DB.rawQuery("Select * from SERVICE_PROVIDER", null)
        }

    fun cancelAppointment(appID: Int): Boolean {
        val db = this.writableDatabase

        // Define the new values for the record
        val values = ContentValues()
        values.put("AppointmentStatus", "Cancelled")

        // Define the selection criteria
        val selection = "AppointmentID = ?"
        val selectionArgs = arrayOf(Integer.toString(appID))

        // Update the record and get the number of rows affected
        val rowsAffected = db.update("APPOINTMENT", values, selection, selectionArgs)

        // Check if any rows were affected by the update
        return if (rowsAffected > 0) {
            // Record updated successfully
            true
        } else {
            // Failed to update record
            false
        }
    }

    fun updateAppointment(
        appID: Int, doDate: String?, doLoc: String?,
        puDate: String?, puLoc: String?, newStatus: String?
    ): Boolean {
        val db = this.writableDatabase

        // Define the new values for the record
        val values = ContentValues()
        values.put("DropOffTimeDate", doDate)
        values.put("DropOffLocation", doLoc)
        values.put("PickUpDateTime", puDate)
        values.put("PickUpLocation", puLoc)
        values.put("AppointmentStatus", newStatus)

        // Define the selection criteria
        val selection = "AppointmentID = ?"
        val selectionArgs = arrayOf(Integer.toString(appID))

        // Update the record and get the number of rows affected
        val rowsAffected = db.update("APPOINTMENT", values, selection, selectionArgs)

        // Check if any rows were affected by the update
        return if (rowsAffected > 0) {
            // Record updated successfully
            true
        } else {
            // Failed to update record
            false
        }
    }

    fun getServiceProviderName(ID: Int): Cursor {
        val DB = this.writableDatabase
        return DB.rawQuery(
            "SELECT serviceProviderFullName FROM SERVICE_PROVIDER WHERE ServiceProviderID = ?",
            arrayOf(ID.toString())
        )
    }

    fun getServiceProviderID(p_email: String): Cursor {
        val DB = this.writableDatabase
        return DB.rawQuery(
            "SELECT serviceProviderID FROM SERVICE_PROVIDER WHERE email = ?",
            arrayOf(p_email)
        )
    }

    //update Service provider profile
    fun updateServiceProviderProfile(
        spID: Int?,
        password: String?,
        name: String?,
        address: String?,
        city: String?,
        contact: String?,
        email: String?
    ): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("serviceProviderPassword", password)
        values.put("serviceProviderFullName", name)
        values.put("street", address)
        values.put("city", city)
        values.put("phone", contact)
        values.put("email", email)

        // define the selection criteria
        val selection = "ServiceProviderID = ?"
        val selectionArgs = arrayOf(
            Integer.toString(
                spID!!
            )
        )
        val count = db.update("SERVICE_PROVIDER", values, selection, selectionArgs)

        // check if the record was updated successfully
        return if (count > 0) {
            true
        } else {
            false
        }
    }

    //
    //update Customer Appointment with Service Provider
    fun updateServiceProviderAppointment(
        appointmentID: Int?, pickUpDateTime: String?,
        pickUpLocation: String?, appointmentStatus: String?
    ): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("PickUpDateTime", pickUpDateTime)
        values.put("PickUpLocation", pickUpLocation)
        values.put("AppointmentStatus", appointmentStatus)

        // define the selection criteria
        val selection = "AppointmentID = ?"
        val selectionArgs = arrayOf(
            Integer.toString(
                appointmentID!!
            )
        )
        val count = db.update("APPOINTMENT", values, selection, selectionArgs)

        // check if the record was updated successfully
        return if (count > 0) {
            true
        } else {
            false
        }
    }

    val customerInfo: Cursor
        //Get customer info
        get() {
            val DB = this.writableDatabase
            return DB.rawQuery("Select * from CUSTOMER", null)
        }

    fun deleteServiceProvider(ID: Int): Boolean {
        val DB = this.writableDatabase
        val rowsDeleted =
            DB.delete("SERVICE_PROVIDER", "ServiceProviderID = ?", arrayOf(ID.toString()))
        return rowsDeleted > 0
    }

    fun getCustomerName(cID: Int): Cursor {
        val DB = this.writableDatabase
        return DB.rawQuery("SELECT name FROM CUSTOMER WHERE Userid = ?", arrayOf(cID.toString()))
    }

    fun getServiceProviderImage(ID: Int): Cursor {
        val DB = this.writableDatabase
        return DB.rawQuery(
            "SELECT imageName FROM SERVICE_PROVIDER WHERE ServiceProviderID = ?",
            arrayOf(ID.toString())
        )
    }

    val isDatabaseEmpty: Boolean
        //check the exist of the data in database
        get() {
            val db = this.readableDatabase
            val count = "SELECT count(*) FROM CUSTOMER"
            val cursor = db.rawQuery(count, null)
            cursor.moveToFirst()
            val rowCount = cursor.getInt(0)
            cursor.close()
            return rowCount == 0
        }
}
