package com.example.carserviceandroidapp

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class CustomerScheduleDropOff : AppCompatActivity() {
    var DB: DBHelper? = null
    var spID = 0
    var userID = 0
    var spName: String? = null
    var sdID: String? = null
    var userName: String? = null
    var custEmail: String? = null
    var fullLoc: String? = null
    var custLoc: String? = null
    var aptID = 0
    var spDetails = ArrayList<String>()
    var location = ArrayList<String>()
    var pickupDateTime = ""
    var pickupLocation = ""
    var pickupReadyDate = ""
    var DropoffTimeDate = ""
    var DropoffLocation: String? = ""
    var BookingDate = ""
    var CancelledDate = ""
    var appointmentType = ""
    var AppointmentStatus = ""
    var ServiceList: String? = null
    var ServiceDetail: String? = null
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_schedule_drop_off)
        DB = DBHelper(this)
        val txtServiceProviderName: TextView =
            findViewById<TextView>(R.id.txtCSDO_ServiceProviderName)
        val txtServiceProviderLocation: TextView =
            findViewById<TextView>(R.id.txtCSDO_ServiceProviderLocation)
        val SpinServiceDetail: Spinner = findViewById<Spinner>(R.id.spinCSDO_ServiceDetail)
        // Spinner SpinServiceLocation = findViewById(R.id.spinCSDO_Location);
        val SpinDate: Spinner = findViewById<Spinner>(R.id.spinCSDO_Date)
        val SpinHours: Spinner = findViewById<Spinner>(R.id.spinCSDO_Time)
        val btnCSDOCancel: Button = findViewById<Button>(R.id.btnCSDOCancel)
        val btnCSDOConfirm: Button = findViewById<Button>(R.id.btnCSDOConfirm)
        btnCSDOConfirm.setBackgroundColor(Color.GREEN)
        btnCSDOCancel.setBackgroundColor(Color.RED)
        val radioGroup: RadioGroup = findViewById<RadioGroup>(R.id.radio_group)
        val otherText: EditText = findViewById<EditText>(R.id.other_text)
        radioGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
                if (checkedId == R.id.radio_button3) {
                    // Show the EditText when Radio Button 3 is selected
                    otherText.setVisibility(View.VISIBLE)
                } else {
                    // Hide the EditText for all other RadioButtons
                    otherText.setVisibility(View.GONE)
                }
            }
        })
        val radioButton1: RadioButton = findViewById<RadioButton>(R.id.radio_button1)
        // Set an OnClickListener on Radio Button 1
        radioButton1.setOnClickListener(View.OnClickListener { // Select Radio Button 1 when its text is clicked
            radioButton1.setChecked(true)
            DropoffLocation = fullLoc
            appointmentType = "Drop Off"
        })
        val radioButton2: RadioButton = findViewById<RadioButton>(R.id.radio_button2)
        // Set an OnClickListener on Radio Button 2
        radioButton2.setOnClickListener(View.OnClickListener { // Select Radio Button 2 when its text is clicked
            radioButton2.setChecked(true)
            DropoffLocation = custLoc
            appointmentType = "Pick Up"
        })
        val radioButton3: RadioButton = findViewById<RadioButton>(R.id.radio_button3)
        // Set an OnClickListener on Radio Button 3
        radioButton3.setOnClickListener(View.OnClickListener { // Select Radio Button 3 when its text is clicked
            radioButton3.setChecked(true)
            appointmentType = "Pick Up"
        })
        otherText.setOnClickListener(View.OnClickListener { radioButton3.setChecked(true) })
        val intent: Intent = getIntent()
        if (intent != null) {
            spID = intent.getStringExtra("SPID").toString().toInt()
        }
        userID = Customer.CustomerID
        displayData()
        displaydata2()
        displayLocation()
        displayLocation2()
        txtServiceProviderName.setText(spName)
        txtServiceProviderLocation.setText(fullLoc)
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, spDetails
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        SpinServiceDetail.setAdapter(adapter)
        val adapter2: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, location
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // SpinServiceLocation.setAdapter(adapter2);
        val calendar = Calendar.getInstance()
        val currentDate = calendar.time

        // DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy EEEE", Locale.getDefault());
        val dateFormat: DateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val CurrDate = dateFormat.format(currentDate)
        val days = arrayOfNulls<String>(7)
        for (i in 0..6) {
            calendar.add(Calendar.DATE, 1)
            val nextDate = calendar.time
            days[i] = dateFormat.format(nextDate)
        }
        val adapter3: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, days)
        SpinDate.setAdapter(adapter3)

        // Get the selected day from the day spinner
        val selectedDay: String = SpinDate.getSelectedItem().toString()

        // Create an array to hold the working hours based on the selected day
        val hours: Array<String>
        hours = if (selectedDay == "Sunday") {
            arrayOf(
                "11:00 AM",
                "11:30 AM",
                "12:00 PM",
                "12:30 PM",
                "01:00 PM",
                "01:30 PM",
                "02:00 PM",
                "02:30 PM",
                "03:00 PM"
            )
        } else {
            arrayOf(
                "10:00 AM",
                "10:30 AM",
                "11:00 AM",
                "11:30 AM",
                "12:00 PM",
                "12:30 PM",
                "01:00 PM",
                "01:30 PM",
                "02:00 PM",
                "02:30 PM",
                "03:00 PM",
                "03:30 PM",
                "04:00 PM"
            )
        }
        val adapter4: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hours)
        SpinHours.setAdapter(adapter4)
        val selectedHour: String = SpinHours.getSelectedItem().toString()
        btnCSDOCancel.setOnClickListener {
            val intent = Intent(this@CustomerScheduleDropOff, CustomerMainMenu::class.java)
            startActivity(intent)
        }
        btnCSDOConfirm.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                //method of confirmation of booking an appointment
                if (!radioButton1.isChecked() && !radioButton2.isChecked() && !radioButton3.isChecked()) {
                    Toast.makeText(
                        this@CustomerScheduleDropOff,
                        "Please fill in the location",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (radioButton3.isChecked() && TextUtils.isEmpty(
                        otherText.getText().toString()
                    )
                ) {
                    Toast.makeText(
                        this@CustomerScheduleDropOff,
                        "Please fill in the location",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    showDialog()
                }
            }

            private fun showDialog() {
                val alert = AlertDialog.Builder(this@CustomerScheduleDropOff)
                val mView: View =
                    getLayoutInflater().inflate(R.layout.confirm_customer_appointment_dialog, null)
                alert.setView(mView)
                val alertDialog = alert.create()
                alertDialog.setCancelable(false)
                ServiceDetail = SpinServiceDetail.getSelectedItem().toString()
                displayData3()
                DropoffTimeDate =
                    SpinDate.getSelectedItem().toString() + " " + SpinHours.getSelectedItem()
                        .toString()
                ServiceList = "SP_" + spID + "_" + sdID
                if (radioButton3.isChecked()) DropoffLocation = otherText.getText().toString()

//                if(SpinServiceLocation.getSelectedItem().toString().equals("Drop Off")) { DropoffLocation = fullLoc;}
//                else {DropoffLocation = custLoc;}
//                appointmentType = SpinServiceLocation.getSelectedItem().toString();
                BookingDate = CurrDate
                AppointmentStatus = "Ongoing"
                DB!!.insertAppointment(
                    userID,
                    spID,
                    pickupDateTime,
                    pickupLocation,
                    pickupReadyDate,
                    DropoffTimeDate,
                    DropoffLocation,
                    BookingDate,
                    CancelledDate,
                    appointmentType,
                    AppointmentStatus
                )
                getAptID()
                DB!!.insertAppointmentDetail(aptID, ServiceList)
                sendEmail()
                mView.findViewById<View>(R.id.okBTN3).setOnClickListener { v: View? ->
                    val intent = Intent(this@CustomerScheduleDropOff, PlainActivity::class.java)
                    startActivity(intent)
                    alertDialog.dismiss()
                }
                alertDialog.show()
            }

            //method for sending email booking confirmation
            private fun sendEmail() {
                Thread {
                    val host = "smtp.mail.yahoo.com"
                    val port = "587"
                    val username = "thienphuocufo@yahoo.com.vn"
                    val password = "wnvqewwhprkhwrqd"
                    val props = Properties()
                    props["mail.smtp.host"] = host
                    props["mail.smtp.port"] = port
                    props["mail.smtp.auth"] = "true"
                    props["mail.smtp.starttls.enable"] = "true"
                    val session = Session.getInstance(props,
                        object : Authenticator() {
                            override fun getPasswordAuthentication(): PasswordAuthentication {
                                return PasswordAuthentication(username, password)
                            }
                        })
                    try {
                        displaydata4()
                        val message: Message = MimeMessage(session)
                        message.setFrom(InternetAddress(username))
                        message.setRecipients(
                            Message.RecipientType.TO,
                            InternetAddress.parse(custEmail)
                        )
                        message.setSubject("Subject: Booking Appointment Confirmation")
                        message.setText(
                            """
    Hello, $userName
    
    This is a confirmation email regarding the appointment you booked at our Service Provider. Here are the details
    
    Service Booked   :   $ServiceDetail
    Booking Time  :   $BookingDate
    Appointment type  :   $appointmentType
    Date and Time  :  $DropoffTimeDate
    Location  :   $DropoffLocation
    
    If you need any further information, please contact us by phone, we will be gladly at your service.
    
    Thank you!
    
    $spName
    """.trimIndent()
                        )
                        Transport.send(message)
                        Log.i(TAG, "Email sent successfully")
                    } catch (e: MessagingException) {
                        Log.e(TAG, "Email sending failed: " + e.message)
                    }
                }.start()
            }
        })
    }

    //method to get the latest appointment ID on the appointment table
    private fun getAptID() {
        val cursor = DB?.appointmentID
        if (cursor!!.count == 0) {
            Toast.makeText(this@CustomerScheduleDropOff, "No Entry Exists", Toast.LENGTH_SHORT)
                .show()
            return
        } else {
            while (cursor!!.moveToNext()) {
                aptID = cursor!!.getString(0).toInt()
            }
        }
    }

    //method to get the list of service providers
    private fun displayData() {
        val cursor = DB?.serviceProviderList
        if (cursor!!.count == 0) {
            Toast.makeText(this@CustomerScheduleDropOff, "No Entry Exists", Toast.LENGTH_SHORT)
                .show()
            return
        } else {
            while (cursor!!.moveToNext()) {
                if (cursor!!.getString(0).toString().toInt() == spID) {
                    spName = cursor!!.getString(2)
                }
            }
        }
    }

    //method to get the service details from the particular service provider
    private fun displaydata2() {
        val cursor = DB?.serviceDetails
        if (cursor!!.count == 0) {
            Toast.makeText(this@CustomerScheduleDropOff, "No Entry Exists", Toast.LENGTH_SHORT)
                .show()
            return
        } else {
            while (cursor!!.moveToNext()) {
                if (cursor!!.getString(2).toString().toInt() == spID) {
                    spDetails.add(cursor!!.getString(0))
                }
            }
        }
    }

    //method to get the customer location
    private fun displayLocation() {
        val cursor = DB?.dropOffLocationCust
        if (cursor!!.count == 0) {
            Toast.makeText(this@CustomerScheduleDropOff, "No Entry Exists", Toast.LENGTH_SHORT)
                .show()
            return
        } else {
            while (cursor!!.moveToNext()) {
                if (cursor!!.getString(0).toString().toInt() == userID) {
                    location.add(cursor!!.getString(1))
                    custLoc = cursor!!.getString(1)
                }
            }
        }
    }

    //method to get the service provider location
    private fun displayLocation2() {
        val cursor = DB?.dropOffLocationSP
        if (cursor!!.count == 0) {
            Toast.makeText(this@CustomerScheduleDropOff, "No Entry Exists", Toast.LENGTH_SHORT)
                .show()
            return
        } else {
            while (cursor!!.moveToNext()) {
                if (cursor!!.getString(0).toString().toInt() == spID) {
                    fullLoc = (cursor!!.getString(1) + ", " + cursor!!.getString(2) + ", "
                        + cursor!!.getString(3) + ", " + cursor!!.getString(4))
                    location.add(fullLoc!!)
                }
            }
        }
    }

    //method to get the service ID from the service details
    fun displayData3() {
        val cursor = DB?.serviceDetails
        if (cursor!!.count == 0) {
            Toast.makeText(this@CustomerScheduleDropOff, "No Entry Exists", Toast.LENGTH_SHORT)
                .show()
            return
        } else {
            while (cursor!!.moveToNext()) {
                if (ServiceDetail == cursor!!.getString(0)) {
                    sdID = cursor!!.getString(3)
                    //                    spName = cursor.getString(2);
                }
            }
        }
    }

    //method to get the customer name
    private fun displaydata4() {
        val cursor = DB?.customerData
        if (cursor!!.count == 0) {
            Toast.makeText(this@CustomerScheduleDropOff, "No Entry Exists", Toast.LENGTH_SHORT)
                .show()
            return
        } else {
            while (cursor!!.moveToNext()) {
                if (cursor!!.getString(0).toInt() == userID) {
                    userName = cursor!!.getString(1)
                    custEmail = cursor!!.getString(3)
                }
            }
        }
    }

    companion object {
        private const val TAG = "RemindEmail"
    }
}