package com.example.carserviceandroidapp

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class CustomerEditAppointment : AppCompatActivity() {
    var dbh = DBHelper(this)
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_edit_appointment)
        val intent: Intent = getIntent()
        val spEmailArr = arrayOfNulls<String>(1)
        val spNameArr = arrayOfNulls<String>(1)
        val dropOffDateTimeArr = arrayOfNulls<String>(1)
        val pickUpDateTimeArr = arrayOfNulls<String>(1)
        val appTypeArr = arrayOfNulls<String>(1)
        val textViewSPName: TextView = findViewById<View>(R.id.textViewSPNameDisplay) as TextView
        val textViewSPAddress: TextView = findViewById<View>(R.id.textViewSPAddress) as TextView
        val textViewSPPhone: TextView = findViewById<View>(R.id.textViewSPCellDisplay) as TextView
        val textViewAppStatus: TextView = findViewById<View>(R.id.textViewStatus) as TextView
        val textViewdropOffDT: TextView = findViewById<View>(R.id.tvDropOffDT) as TextView
        val textViewdropOffLoc: TextView = findViewById<View>(R.id.textViewDropOffLoc) as TextView
        val textViewPickupDT: TextView = findViewById<View>(R.id.textViewPickupTime) as TextView
        val textViewPickupLoc: TextView = findViewById<View>(R.id.tvPickupLocation) as TextView
        val textViewServiceDetails: TextView =
            findViewById<View>(R.id.textViewServiceDetails) as TextView
        val textViewAppStatusDown: TextView =
            findViewById<View>(R.id.editTextAppointmentStatus) as TextView
        val textViewAppontType: TextView = findViewById<View>(R.id.tvAppointType) as TextView
        val appID = intent.getIntExtra("AppId", 0)
        val serviceProviderName: String? = intent.getStringExtra("ServiceProviderName")
        val serviceProviderAddress: String? = intent.getStringExtra("SPAddress")
        val appointmentStatus = intent.getStringExtra("AppStatus")
        val dropoffDate = intent.getStringExtra("DropoffD")
        val dropoffT: String? = intent.getStringExtra("DropoffT")
        val pickupD = intent.getStringExtra("PickupD")
        val pickupT: String? = intent.getStringExtra("PickupT")
        val dropoffLoc: String? = intent.getStringExtra("DropoffLoc")
        val pickupLoc: String? = intent.getStringExtra("PickupLoc")
        val serviceDetails: String? = intent.getStringExtra("ServiceDet")
        val spPhone: String? = intent.getStringExtra("SPPhone")
        val spEmail = intent.getStringExtra("SPEmail")
        val appType = intent.getStringExtra("AppType")
        textViewSPName.setText(serviceProviderName)
        textViewSPAddress.setText(serviceProviderAddress)
        textViewSPPhone.setText("Contact: $spPhone")
        textViewAppStatus.setText(appointmentStatus)
        textViewdropOffDT.setText("$dropoffDate  $dropoffT")
        textViewdropOffLoc.setText(dropoffLoc)
        textViewPickupDT.setText("$pickupD  $pickupT")
        textViewPickupLoc.setText(pickupLoc)
        textViewServiceDetails.setText(serviceDetails)
        textViewAppStatusDown.setText(appointmentStatus)
        textViewAppontType.setText(appType)
        textViewAppStatus.setTextColor(Color.WHITE)
        val drawable = GradientDrawable()
        drawable.setShape(GradientDrawable.RECTANGLE)
        drawable.setCornerRadius(20f)
        val chosenColor = Color.rgb(247, 201, 16)
        drawable.setColor(chosenColor)
        textViewAppStatus.setBackgroundDrawable(drawable)
        val btnUpdateApp = findViewById<View>(R.id.buttonUpdateFirst) as Button
        btnUpdateApp.setOnClickListener {
            val intent =
                Intent(this@CustomerEditAppointment, CustomerEditAppointmentForm::class.java)
            intent.putExtra("AppId", getIntent().getIntExtra("AppId", 0))
            intent.putExtra(
                "ServiceProviderName",
                getIntent().getStringExtra("ServiceProviderName")
            )
            intent.putExtra("SPAddress", getIntent().getStringExtra("SPAddress"))
            intent.putExtra("AppStatus", getIntent().getStringExtra("AppStatus"))
            intent.putExtra("DropoffD", getIntent().getStringExtra("DropoffD"))
            intent.putExtra("DropoffT", getIntent().getStringExtra("DropoffT"))
            intent.putExtra("PickupD", getIntent().getStringExtra("PickupD"))
            intent.putExtra("PickupT", getIntent().getStringExtra("PickupT"))
            intent.putExtra("DropoffLoc", getIntent().getStringExtra("DropoffLoc"))
            intent.putExtra("PickupLoc", getIntent().getStringExtra("PickupLoc"))
            intent.putExtra("ServiceDet", getIntent().getStringExtra("ServiceDet"))
            intent.putExtra("SPPhone", getIntent().getStringExtra("SPPhone"))
            intent.putExtra("SPEmail", getIntent().getStringExtra("SPEmail"))
            intent.putExtra("AppType", getIntent().getStringExtra("AppType"))
            //place cell number here
            //place email address
            startActivity(intent)
        }
        val appIDArr = IntArray(1)
        appIDArr[0] = appID
        val status = arrayOf(appointmentStatus)
        spEmailArr[0] = spEmail
        spNameArr[0] = serviceProviderName
        dropOffDateTimeArr[0] = dropoffDate
        pickUpDateTimeArr[0] = pickupD
        appTypeArr[0] = appType
        val btnCancelApp = findViewById<View>(R.id.buttonCancel) as Button
        btnCancelApp.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                showDialogCancelApp()
            }

            private fun showDialogCancelApp() {
                if (textViewPickupDT.getText().toString().replace(" ", "").isEmpty()) {
                    val alert = AlertDialog.Builder(this@CustomerEditAppointment)
                    val mView: View =
                        getLayoutInflater().inflate(R.layout.customer_cancelappointment, null)
                    alert.setView(mView)
                    val alertDialog = alert.create()
                    alertDialog.setCancelable(false)
                    mView.findViewById<View>(R.id.noButton)
                        .setOnClickListener { v: View? -> alertDialog.dismiss() }
                    mView.findViewById<View>(R.id.yesButton).setOnClickListener { v: View? ->
                        // Get the ID of the row to update
                        val appIdCurrent = appIDArr[0]
                        val isUpdated = dbh.cancelAppointment(appIdCurrent)
                        if (isUpdated) {
                            Thread(Runnable {
                                val stringReceiverEmail = spEmailArr[0]
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
                                        //Authenticating the password
                                        override fun getPasswordAuthentication(): PasswordAuthentication {
                                            return PasswordAuthentication(username, password)
                                        }
                                    })

                                try {
                                    val message: Message = MimeMessage(session)
                                    message.setFrom(InternetAddress(username))
                                    message.setRecipients(
                                        Message.RecipientType.TO,
                                        InternetAddress.parse(stringReceiverEmail)
                                    )
                                    message.setSubject("APPOINTMENT CANCELLATION MESSAGE")
                                    message.setText(
                                        ("Hello " + spNameArr[0] + ", \n\nThis is to inform that Appointment " + appIDArr[0] + " set on " + dropOffDateTimeArr[0] +
                                            " has been cancelled by the Customer.  Check you GARK account for details. \n\n Cheers!\nGARK")
                                    )
                                    Transport.send(message)
                                    Log.i(TAG, "Email sent successfully")
                                } catch (e: MessagingException) {
                                    Log.e(TAG, "Email sending failed: " + e.message)
                                }
                            }).start()
                            Toast.makeText(
                                this@CustomerEditAppointment,
                                "Appointment Cancelled",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(
                                Intent(
                                    this@CustomerEditAppointment,
                                    PlainActivity::class.java
                                )
                            )
                        } else {
                            Toast.makeText(
                                this@CustomerEditAppointment,
                                "Unsuccessful Cancellation",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        alertDialog.dismiss()
                    }
                    alertDialog.show()
                } else {
                    Toast.makeText(
                        this@CustomerEditAppointment,
                        "You can only update this appointment but not cancel.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
        val remindApp = findViewById<View>(R.id.butnRemind) as Button
        remindApp.setOnClickListener {
            val alert = AlertDialog.Builder(this@CustomerEditAppointment)
            val mView: View =
                getLayoutInflater().inflate(R.layout.customer_appointment_reminder_dialogue, null)
            alert.setView(mView)
            val alertDialog = alert.create()
            alertDialog.setCancelable(false)
            mView.findViewById<View>(R.id.okBTNRemind)
                .setOnClickListener { view: View? -> alertDialog.dismiss() }
            alertDialog.show()
            Thread {
                val stringReceiverEmail = spEmailArr[0]
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
                        //Authenticating the password
                        override fun getPasswordAuthentication(): PasswordAuthentication {
                            return PasswordAuthentication(username, password)
                        }
                    })
                try {
                    val message: Message = MimeMessage(session)
                    message.setFrom(InternetAddress(username))
                    message.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(stringReceiverEmail)
                    )
                    message.setSubject("Appointment Reminder")
                    if (textViewPickupDT.getText().toString().replace(" ", "").isEmpty()) {
                        message.setText(
                            "Hello " + spNameArr[0] + ", \n\nThis a reminder of the " + appTypeArr[0] + " appointment set on " + dropOffDateTimeArr[0] +
                                " under Appointment ID " + appIDArr[0] + ". " + "Check your GARK account for details. \n\n Cheers!\nGARK"
                        )
                    } else {
                        message.setText(
                            "Hello " + spNameArr[0] + ", \n\nThis a reminder of our after-servicing pickup appointment on " + pickUpDateTimeArr[0] +
                                " under Appointment ID " + appIDArr[0] + ". " + "Check your GARK account for details. \n\n Cheers!\nGARK"
                        )
                    }
                    Transport.send(message)
                    Log.i(TAG, "Email sent successfully")
                } catch (e: MessagingException) {
                    Log.e(TAG, "Email sending failed: " + e.message)
                }
            }.start()
        }
    }

    companion object {
        private const val TAG = "RemindEmail"
    }
}