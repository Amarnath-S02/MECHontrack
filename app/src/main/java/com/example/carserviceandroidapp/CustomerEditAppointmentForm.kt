package com.example.carserviceandroidapp
//import static android.content.ContentValues.TAG;
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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

class CustomerEditAppointmentForm : AppCompatActivity() {
    var dbh: DBHelper = DBHelper(this)
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_edit_appointment_form)
        val tvStatusForm: TextView = findViewById<View>(R.id.textViewStatusForm) as TextView
        val tvSPNameDispForm: TextView =
            findViewById<View>(R.id.textViewSPNameDisplayForm) as TextView
        val tvSPAddressForm: TextView = findViewById<View>(R.id.textViewSPAddressForm) as TextView
        val tvSPCellDispForm: TextView =
            findViewById<View>(R.id.textViewSPCellDisplayForm) as TextView
        val etDODateForm: EditText = findViewById<View>(R.id.tvDropOffDateForm) as EditText
        val etDOTimeForm: EditText = findViewById<View>(R.id.etDropOffTimeForm) as EditText
        val etDOLocForm: EditText = findViewById<View>(R.id.textViewDropOffLocForm) as EditText
        val etPUDateForm: EditText = findViewById<View>(R.id.textViewPickupDateForm) as EditText
        val etPUTimeForm: EditText = findViewById<View>(R.id.etPickUpTimeForm) as EditText
        val tvAppStatusDownForm: TextView =
            findViewById<View>(R.id.editTextAppointmentStatusForm) as TextView
        val etPULocForm: EditText = findViewById<View>(R.id.tvPickupLocationForm) as EditText
        val tvAppType: TextView = findViewById<View>(R.id.tvAppointTypeForm) as TextView
        //
        val appIdArr: IntArray = IntArray(1)
        var appId: Int = 0
        //
        val spEmailArr: Array<String?> = arrayOfNulls(1)
        var spEmail: String? = ""
        //
        val spNameArr: Array<String?> = arrayOfNulls(1)
        var serviceProviderName: String? = ""
        val intent: Intent? = getIntent()
        if (intent != null) {
            appId = intent.getIntExtra("AppId", 0)
            serviceProviderName = intent.getStringExtra("ServiceProviderName")
            val serviceProviderAddress: String? = intent.getStringExtra("SPAddress")
            val appointmentStatus: String? = intent.getStringExtra("AppStatus")
            val appType: String? = intent.getStringExtra("AppType")
            val dropoffDateTime: Array<String> =
                intent.getStringExtra("DropoffD")!!.split(" ".toRegex())
                    .dropLastWhile({ it.isEmpty() }).toTypedArray()
            val dropOffDate: String = dropoffDateTime.get(0)
            val droffOffTime: String = dropoffDateTime.get(1) + " " + dropoffDateTime.get(2)
            val dropoffT: String? = intent.getStringExtra("DropoffT")
            val pickupDateTimeString: String? = intent.getStringExtra("PickupD")
            var pickupDateTime: Array<String?> = arrayOfNulls(3)
            var pickupDate: String?
            var pickupTime: String?
            if (pickupDateTimeString != null && !pickupDateTimeString.isEmpty()) {
                pickupDateTime = intent.getStringExtra("PickupD")!!.split(" ".toRegex())
                    .dropLastWhile({ it.isEmpty() }).toTypedArray()
                pickupDate = pickupDateTime.get(0)
                pickupTime = pickupDateTime.get(1) + " " + pickupDateTime.get(2)
            } else {
                pickupDate = ""
                pickupTime = ""
            }
            val pickupT: String? = intent.getStringExtra("PickupT")
            val dropoffLoc: String? = intent.getStringExtra("DropoffLoc")
            val pickupLoc: String? = intent.getStringExtra("PickupLoc")
            val serviceDetails: String? = intent.getStringExtra("ServiceDet")
            val spPhone: String? = intent.getStringExtra("SPPhone")
            spEmail = intent.getStringExtra("SPEmail")
            tvStatusForm.setTextColor(Color.WHITE)
            val drawable: GradientDrawable = GradientDrawable()
            drawable.setShape(GradientDrawable.RECTANGLE)
            drawable.setCornerRadius(20f)
            var chosenColor: Int = 0
            if ((appointmentStatus == "Ongoing")) {
                chosenColor = Color.rgb(247, 201, 16)
            } else if ((appointmentStatus == "Completed")) {
                chosenColor = Color.RED
            }
            drawable.setColor(chosenColor)
            tvStatusForm.setBackgroundDrawable(drawable)
            tvStatusForm.setText(appointmentStatus)
            tvSPNameDispForm.setText(serviceProviderName)
            tvSPAddressForm.setText(serviceProviderAddress)
            tvSPCellDispForm.setText(spPhone)
            etDODateForm.setText(dropOffDate)
            etDOTimeForm.setText(droffOffTime)
            tvAppType.setText(appType)
            etDOLocForm.setText(dropoffLoc)
            var newDOLoc: String? = ""
            etPUDateForm.setText(pickupDate)
            etPUTimeForm.setText(pickupTime)
            val newDODateTime: Array<String?> = arrayOfNulls(2)
            if (TextUtils.isEmpty(etPUDateForm.getText().toString())) {
                etDODateForm.setEnabled(true)
                etDOTimeForm.setEnabled(true)
                etDOLocForm.setEnabled(true)
                etDODateForm.setOnClickListener(object : View.OnClickListener {
                    public override fun onClick(v: View) {
                        val selectedDate: Calendar = Calendar.getInstance()
                        // Create a new DatePickerDialog
                        val datePickerDialog: DatePickerDialog = DatePickerDialog(
                            this@CustomerEditAppointmentForm,
                            object : DatePickerDialog.OnDateSetListener {
                                public override fun onDateSet(
                                    view: DatePicker,
                                    year: Int,
                                    month: Int,
                                    dayOfMonth: Int
                                ) {
                                    // Set the year, month, and day fields of the selectedDate Calendar object
                                    selectedDate.set(Calendar.YEAR, year)
                                    selectedDate.set(Calendar.MONTH, month)
                                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                                    // Do something with the selected date
                                    val dateFormat: SimpleDateFormat =
                                        SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                                    val selectedDateAsString: String =
                                        dateFormat.format(selectedDate.getTime())
                                    etDODateForm.setText(selectedDateAsString)
                                    newDODateTime[0] = selectedDateAsString
                                }
                            },
                            selectedDate.get(Calendar.YEAR),
                            selectedDate.get(Calendar.MONTH),
                            selectedDate.get(
                                Calendar.DAY_OF_MONTH
                            )
                        )

                        // Show the DatePickerDialog
                        datePickerDialog.show()
                    }
                })
                val selectedTime: Calendar = Calendar.getInstance()
                // Set an OnClickListener to show the TimePickerDialog when the TextView is clicked
                etDOTimeForm.setOnClickListener(object : View.OnClickListener {
                    public override fun onClick(v: View) {
                        // Create a new TimePickerDialog
                        val timePickerDialog: TimePickerDialog = TimePickerDialog(
                            this@CustomerEditAppointmentForm,
                            object : TimePickerDialog.OnTimeSetListener {
                                public override fun onTimeSet(
                                    view: TimePicker,
                                    hourOfDay: Int,
                                    minute: Int
                                ) {
                                    // Set the hour and minute fields of the selectedTime Calendar object
                                    selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                    selectedTime.set(Calendar.MINUTE, minute)

                                    // Do something with the selected time
                                    val timeOutput: String = String.format(
                                        "%02d:%02d %s",
                                        if ((hourOfDay == 0 || hourOfDay == 12)) 12 else hourOfDay % 12,
                                        minute,
                                        if ((hourOfDay < 12)) "AM" else "PM"
                                    )
                                    newDODateTime[1] = timeOutput
                                    etDOTimeForm.setText(timeOutput)
                                }
                            },
                            selectedTime.get(Calendar.HOUR_OF_DAY),
                            selectedTime.get(Calendar.MINUTE),
                            true
                        )

                        // Show the TimePickerDialog
                        timePickerDialog.show()
                    }
                })
                newDOLoc = etDOLocForm.getText().toString()
            } else {
                etDODateForm.setEnabled(false)
                etDOTimeForm.setEnabled(false)
                etDOLocForm.setEnabled(false)
            }
            val newDODateTimeComb: String = newDODateTime.get(0) + newDODateTime.get(1)
            etPULocForm.setText(pickupLoc)
            val newPULoc: String
            val newPUDateTimeArray: Array<String?> = arrayOfNulls(2)
            if (etPUDateForm.getText().toString().isEmpty()) {
                etPUDateForm.setHint("")
                etPUDateForm.setEnabled(false)
                etPUTimeForm.setHint("")
                etPUTimeForm.setEnabled(false)
                etPULocForm.setHint("")
                etPULocForm.setEnabled(false)
            } else {
                etPUDateForm.setEnabled(true)
                etPUTimeForm.setEnabled(true)
                //etPULocForm.setEnabled(true);
                etPUDateForm.setOnClickListener(object : View.OnClickListener {
                    public override fun onClick(v: View) {
                        val selectedDate: Calendar = Calendar.getInstance()

                        // Create a new DatePickerDialog
                        val datePickerDialog: DatePickerDialog = DatePickerDialog(
                            this@CustomerEditAppointmentForm,
                            object : DatePickerDialog.OnDateSetListener {
                                public override fun onDateSet(
                                    view: DatePicker,
                                    year: Int,
                                    month: Int,
                                    dayOfMonth: Int
                                ) {
                                    // Set the year, month, and day fields of the selectedDate Calendar object
                                    selectedDate.set(Calendar.YEAR, year)
                                    selectedDate.set(Calendar.MONTH, month)
                                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                                    // Do something with the selected date
                                    val dateFormat: SimpleDateFormat =
                                        SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                                    val selectedDateAsString: String =
                                        dateFormat.format(selectedDate.getTime())
                                    etPUDateForm.setText(selectedDateAsString)
                                    newPUDateTimeArray[0] = selectedDateAsString
                                }
                            },
                            selectedDate.get(Calendar.YEAR),
                            selectedDate.get(Calendar.MONTH),
                            selectedDate.get(
                                Calendar.DAY_OF_MONTH
                            )
                        )

                        // Show the DatePickerDialog
                        datePickerDialog.show()
                    }
                })
                val selectedTime: Calendar = Calendar.getInstance()
                // Set an OnClickListener to show the TimePickerDialog when the TextView is clicked
                etPUTimeForm.setOnClickListener(object : View.OnClickListener {
                    public override fun onClick(v: View) {
                        // Create a new TimePickerDialog
                        val timePickerDialog: TimePickerDialog = TimePickerDialog(
                            this@CustomerEditAppointmentForm,
                            object : TimePickerDialog.OnTimeSetListener {
                                public override fun onTimeSet(
                                    view: TimePicker,
                                    hourOfDay: Int,
                                    minute: Int
                                ) {
                                    // Set the hour and minute fields of the selectedTime Calendar object
                                    selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                    selectedTime.set(Calendar.MINUTE, minute)

                                    // Do something with the selected time
                                    val dateOutput: String = String.format(
                                        "%02d:%02d %s",
                                        if ((hourOfDay == 0 || hourOfDay == 12)) 12 else hourOfDay % 12,
                                        minute,
                                        if ((hourOfDay < 12)) "AM" else "PM"
                                    )
                                    newPUDateTimeArray[1] = dateOutput
                                    etPUTimeForm.setText(dateOutput)
                                }
                            },
                            selectedTime.get(Calendar.HOUR_OF_DAY),
                            selectedTime.get(Calendar.MINUTE),
                            true
                        )

                        // Show the TimePickerDialog
                        timePickerDialog.show()
                    }
                })
                newPULoc = etPULocForm.getText().toString()
            }
            val tvServDetailsForm: TextView =
                findViewById<View>(R.id.textViewServiceDetailsForm) as TextView
            tvServDetailsForm.setOnClickListener(object : View.OnClickListener {
                public override fun onClick(v: View) {
                    Toast.makeText(
                        this@CustomerEditAppointmentForm,
                        "Book another appointment if you want to change a Service",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
            tvServDetailsForm.setText(serviceDetails)
            tvAppStatusDownForm.setText(appointmentStatus)
            tvAppStatusDownForm.setOnClickListener(object : View.OnClickListener {
                public override fun onClick(v: View) {
                    // Create the RadioGroup
                    val radioGroup: RadioGroup = RadioGroup(this@CustomerEditAppointmentForm)
                    radioGroup.orientation = RadioGroup.VERTICAL

                    // Create the RadioButtons and add them to the RadioGroup
                    val radioButton1: RadioButton = RadioButton(this@CustomerEditAppointmentForm)
                    radioButton1.setText("Ongoing")
                    radioGroup.addView(radioButton1)
                    val radioButton2: RadioButton = RadioButton(this@CustomerEditAppointmentForm)
                    radioButton2.setText("Completed")
                    radioGroup.addView(radioButton2)


                    // Show the RadioGroup in an AlertDialog
                    val builder: AlertDialog.Builder =
                        AlertDialog.Builder(this@CustomerEditAppointmentForm)
                    builder.setView(radioGroup)
                    builder.setTitle("Select an option")
                    builder.setPositiveButton("OK", object : DialogInterface.OnClickListener {
                        public override fun onClick(dialog: DialogInterface, which: Int) {
                            // Get the selected RadioButton and do something with its text
                            val selectedId: Int = radioGroup.getCheckedRadioButtonId()
                            val selectedRadioButton: RadioButton =
                                radioGroup.findViewById<View>(selectedId) as RadioButton
                            val selectedOption: String = selectedRadioButton.getText().toString()
                            tvAppStatusDownForm.setText(selectedOption)
                            tvStatusForm.setText(selectedOption)
                            if ((selectedOption == "Ongoing")) {
                                drawable.setColor(Color.rgb(247, 201, 16))
                            } else if ((selectedOption == "Completed")) {
                                drawable.setColor(Color.rgb(101, 207, 114))
                            }
                        }
                    })
                    builder.setNegativeButton("Cancel", null)
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }
            })
        }
        appIdArr[0] = appId
        spEmailArr[0] = spEmail
        spNameArr[0] = serviceProviderName
        val buttonUpdate: Button = findViewById<View>(R.id.buttonUpdateSecond) as Button
        buttonUpdate.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                val doDate: String
                val doTime: String
                val doLoc: String
                val puDate: String
                val puTime: String
                val puLoc: String
                var puDateTime: String? = ""
                var doDateTime: String? = ""
                val statusDown: String = tvAppStatusDownForm.getText().toString()
                if (!etDODateForm.getText().toString().isEmpty()) {
                    doDate = etDODateForm.getText().toString()
                } else {
                    doDate = ""
                }
                if (!etDOTimeForm.getText().toString().isEmpty()) {
                    doTime = etDOTimeForm.getText().toString()
                } else {
                    doTime = ""
                }
                if (!etDOLocForm.getText().toString().isEmpty()) {
                    doLoc = etDOLocForm.getText().toString()
                } else {
                    doLoc = ""
                }
                if (!etPUDateForm.getText().toString().isEmpty()) {
                    puDate = etPUDateForm.getText().toString()
                } else {
                    puDate = ""
                }
                if (!etPUTimeForm.getText().toString().isEmpty()) {
                    puTime = etPUTimeForm.getText().toString()
                    puDateTime = puDate + " " + puTime
                } else {
                    puTime = ""
                    puDateTime = ""
                }
                if (!etPULocForm.getText().toString().isEmpty()) {
                    puLoc = etPULocForm.getText().toString()
                } else {
                    puLoc = ""
                }
                doDateTime = doDate + " " + doTime
                val isUpdated: Boolean = dbh.updateAppointment(
                    appIdArr.get(0),
                    doDateTime,
                    doLoc,
                    puDateTime,
                    puLoc,
                    statusDown
                )
                if (isUpdated) {
                    Thread(object : Runnable {
                        public override fun run() {
                            val stringReceiverEmail: String? = spEmailArr.get(0)
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
                                    InternetAddress.parse(stringReceiverEmail)
                                )
                                message.setSubject("Appointment Update")
                                message.setText(
                                    ("Hello " + spNameArr.get(0) + ", \n\nThis is to inform that there are changes to Appointment ID: " + appIdArr.get(
                                        0
                                    ) + ". Check GARK to view the changes" +
                                        ". \n\n Cheers!\nGARK")
                                )
                                Transport.send(message)
                                Log.i(TAG, "Email sent successfully")
                            } catch (e: MessagingException) {
                                Log.e(TAG, "Email sending failed: " + e.message)
                            }
                        }
                    }).start()
                    Toast.makeText(
                        this@CustomerEditAppointmentForm,
                        "Record Updated",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(
                        Intent(
                            this@CustomerEditAppointmentForm,
                            PlainActivity::class.java
                        )
                    )
                } else {
                    Toast.makeText(
                        this@CustomerEditAppointmentForm,
                        "Record Not Updated",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    companion object {
        private val TAG: String = "RemindEmail"
    }
}