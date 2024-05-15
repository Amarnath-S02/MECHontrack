package com.example.carserviceandroidapp

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

class ProviderEditAppointment() : AppCompatActivity() {
    var v_customerName: String? = null
    var v_customerContact: String? = null
    var v_customerEmail: String? = null
    var v_dropOffDateTime: String? = null
    var v_dropOffLocation: String? = null
    var v_pickUpDateTime: String? = null
    var v_pickUpLocation: String? = null
    var v_selectedServices: String? = null
    var v_appointmentStatus: String? = null
    var v_serviceTypeBook: String? = null
    var v_serviceTypeDone: String? = null
    var v_appointmentID: Int = 0
    lateinit var pickUpDateTime: EditText
    lateinit var pickUpLocation: EditText
    var dbHelper: DBHelper? = null
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provider_edit_appointment)
        dbHelper = DBHelper(this)
        val intent: Intent? = getIntent()
        val appointmentStatusHead: TextView =
            findViewById<View>(R.id.appointmentStatusHead) as TextView
        val customerName: TextView = findViewById<View>(R.id.customerName) as TextView
        val customerContact: TextView = findViewById<View>(R.id.contact) as TextView
        val customerEmail: TextView = findViewById<View>(R.id.email) as TextView
        val dropOffDateTime: TextView = findViewById<View>(R.id.dropOffDateTime) as TextView
        val dropOffLocation: TextView = findViewById<View>(R.id.dropOffLocation) as TextView
        val serviceTypeBook: TextView = findViewById<View>(R.id.serviceTypeBook) as TextView
        val serviceTypeDone: TextView = findViewById<View>(R.id.serviceTypeDone) as TextView
        pickUpDateTime = findViewById<EditText>(R.id.pickUpDateTime)
        pickUpLocation = findViewById<EditText>(R.id.pickUpLocation)
        val selectedServices: TextView = findViewById<View>(R.id.selectedServices) as TextView
        val appointmentStatusEdit: TextView = findViewById<TextView>(R.id.appointmentStatusEdit)
        val btnUpdate: Button = findViewById<Button>(R.id.btnUpdate)
        val btnCancel: Button = findViewById<Button>(R.id.btnCancel)
        val btnRemind: Button = findViewById<Button>(R.id.btnEmail)
        if (intent != null) {
            v_appointmentID = intent.getIntExtra("AppointmentID", 0)
            v_appointmentStatus = intent.getStringExtra("AppointmentStatus")
            v_customerName = intent.getStringExtra("CustomerName")
            v_customerContact = intent.getStringExtra("CustomerContact")
            v_customerEmail = intent.getStringExtra("CustomerEmail")
            v_dropOffDateTime = intent.getStringExtra("DropOffDateTime")
            v_dropOffLocation = intent.getStringExtra("DropOffLocation")
            v_pickUpDateTime = intent.getStringExtra("PickupDateTime")
            v_pickUpLocation = intent.getStringExtra("PickUpLocation")
            v_selectedServices = intent.getStringExtra("SelectedService")
            v_serviceTypeBook = intent.getStringExtra("AppointmentType")
            v_serviceTypeDone = intent.getStringExtra("AppointmentType")


            //add to text view
            appointmentStatusHead.setText(v_appointmentStatus)
            customerName.setText(v_customerName)
            customerContact.setText("Contact: " + v_customerContact)
            customerEmail.setText(v_customerEmail)
            dropOffDateTime.setText(v_dropOffDateTime)
            dropOffLocation.setText(v_dropOffLocation)
            pickUpDateTime.setText(v_pickUpDateTime)
            pickUpLocation.setText(v_pickUpLocation)
            selectedServices.setText(v_selectedServices)
            appointmentStatusEdit.setText(v_appointmentStatus)
            serviceTypeBook.setText(v_serviceTypeBook)
            serviceTypeDone.setText(v_serviceTypeDone)
            appointmentStatusHead.setTextColor(Color.WHITE)
            val drawable: GradientDrawable = GradientDrawable()
            drawable.setShape(GradientDrawable.RECTANGLE)
            drawable.setCornerRadius(20f)
            val chosenColor: Int = Color.rgb(247, 201, 16)
            drawable.setColor(chosenColor)
            appointmentStatusHead.setBackgroundDrawable(drawable)
        }

        //Remind by email
        btnRemind.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                Thread(object : Runnable {
                    public override fun run() {
                        val host: String = "smtp.mail.yahoo.com"
                        val port: String = "587"
                        val username: String = "thienphuocufo@yahoo.com.vn"
                        val password: String = "wnvqewwhprkhwrqd"
                        val props: Properties = Properties()
                        props.put("mail.smtp.host", host)
                        props.put("mail.smtp.port", port)
                        props.put("mail.smtp.auth", "true")
                        props.put("mail.smtp.starttls.enable", "true")
                        val session = Session.getInstance(props,
                            object : Authenticator() {
                                override fun getPasswordAuthentication(): PasswordAuthentication {
                                    return PasswordAuthentication(username, password)
                                }
                            })
                        try {
                            val message: Message = MimeMessage(session)
                            message.setFrom(InternetAddress(username))
                            message.setRecipients(
                                Message.RecipientType.TO,
                                InternetAddress.parse("ralphgradien01@gmail.com")
                            )
                            message.setSubject("Car Service Remind")
                            message.setText(
                                ("Dear " + v_customerName + ",\n\n" +
                                    "This is a reminder email about the service you booked at our company. \n\n" +
                                    "Service booked              : " + v_selectedServices + "\n" +
                                    "Service booked schedule: " + v_dropOffDateTime + "\n" +
                                    "Service booked location  : " + v_dropOffLocation + "\n" +
                                    "Service type booked       : " + v_serviceTypeBook + "\n" +
                                    "Service done schedule    : " + v_pickUpDateTime + "\n" +
                                    "Drop-off/Pick-up location: " + v_pickUpLocation + "\n" +
                                    "After serviced                : " + v_serviceTypeDone + "\n\n" +
                                    "If you need any further information, please contact us by phone or email.\n\n" +
                                    "Thank you!")
                            )
                            Transport.send(message)
                            Log.i(TAG, "Email sent successfully")
                        } catch (e: MessagingException) {
                            Log.e(TAG, "Email sending failed: " + e.message)
                        }
                    }
                }).start()
                Toast.makeText(this@ProviderEditAppointment, "Email sent", Toast.LENGTH_LONG)
                    .show()
                finish()
            }
        })

        //set radio button for Appointment Status change
        appointmentStatusEdit.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                // Create the RadioGroup
                val radioGroup: RadioGroup = RadioGroup(this@ProviderEditAppointment)
                radioGroup.setOrientation(RadioGroup.VERTICAL)
                // Create the RadioButtons and add them to the RadioGroup
                val rdOngoing: RadioButton = RadioButton(this@ProviderEditAppointment)
                rdOngoing.setText("Ongoing")
                radioGroup.addView(rdOngoing)
                val rdCompleted: RadioButton = RadioButton(this@ProviderEditAppointment)
                rdCompleted.setText("Completed")
                radioGroup.addView(rdCompleted)

                // Show the RadioGroup in an AlertDialog
                val builder: AlertDialog.Builder =
                    AlertDialog.Builder(this@ProviderEditAppointment)
                builder.setView(radioGroup)
                builder.setTitle("Select Appointment Status")
                builder.setPositiveButton("OK", object : DialogInterface.OnClickListener {
                    public override fun onClick(dialog: DialogInterface, which: Int) {
                        // Get the selected RadioButton
                        val selectedStatus: Int = radioGroup.getCheckedRadioButtonId()
                        val selectedRadioButton: RadioButton =
                            radioGroup.findViewById<View>(selectedStatus) as RadioButton
                        val selectedOption: String = selectedRadioButton.getText().toString()
                        appointmentStatusHead.setText(selectedOption)
                        appointmentStatusEdit.setText(selectedOption)

                        //set color for the status
                        val drawable: GradientDrawable = GradientDrawable()
                        if ((selectedOption == "Ongoing")) {
                            drawable.setColor(Color.rgb(247, 201, 16))
                        } else if ((selectedOption == "Completed")) {
                            drawable.setColor(Color.GREEN)
                        }
                    }
                })
                builder.setNegativeButton("Cancel", null)
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        })
        btnUpdate.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                val checkupdatedata: Boolean? = dbHelper!!.updateServiceProviderAppointment(
                    v_appointmentID,
                    pickUpDateTime.getText().toString(),
                    pickUpLocation.getText().toString(),
                    appointmentStatusEdit.getText().toString()
                )
                if (checkupdatedata == true) {
                    Toast.makeText(
                        this@ProviderEditAppointment,
                        "Updated Successfully",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this@ProviderEditAppointment,
                        "Update failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
                finish()
            }
        })
        val appIDArr: IntArray = IntArray(1)
        appIDArr[0] = v_appointmentID
        val status: Array<String?> = arrayOf(v_appointmentStatus)
        btnCancel.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                if (pickUpDateTime.getText().toString().replace(" ", "").isEmpty()) {
                    val builder: AlertDialog.Builder =
                        AlertDialog.Builder(this@ProviderEditAppointment, R.style.MyDialogStyle)
                    builder.setTitle("Cancel the appointment")
                        .setMessage("Are you sure you want to cancel this appointment?")
                        .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                            public override fun onClick(dialog: DialogInterface, id: Int) {
                                // Get the ID of the row to update
                                val appIdCurrent: Int = appIDArr.get(0)
                                val isUpdated: Boolean = dbHelper!!.cancelAppointment(appIdCurrent)
                                if (isUpdated) {
                                    Toast.makeText(
                                        this@ProviderEditAppointment,
                                        "Successfully Cancelled",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this@ProviderEditAppointment,
                                        "Failure to Cancel",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                dialog.dismiss()
                            }
                        })
                        .setNegativeButton("No", object : DialogInterface.OnClickListener {
                            public override fun onClick(dialog: DialogInterface, id: Int) {
                                dialog.dismiss()
                            }
                        })
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                } else {
                    Toast.makeText(
                        this@ProviderEditAppointment,
                        "Service in progress, you cannot cancel the appointment",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

    companion object {
        private val TAG: String = "RemindEmail"
    }
}
