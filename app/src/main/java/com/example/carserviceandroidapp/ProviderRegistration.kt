package com.example.carserviceandroidapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class ProviderRegistration : AppCompatActivity() {
    lateinit var providerServices: MaterialCardView
    lateinit var selectedServices: BooleanArray
    var selectedLocation = ArrayList<Int>()
    var selectedServiceList = ArrayList<StringBuilder>()
    var dbHelper = DBHelper(this@ProviderRegistration)
    var serviceProvide = arrayOf(
        "Full Brake Check",
        "Tire Rotation",
        "Battery Replacement",
        "Air Filter Replacement",
        "Wheel Alignment",
        "Spark Plug Replacement",
        "Coolant Flush",
        "Transmission Service",
        "Fuel Injection Service",
        "Wheel Replacement",
        "Brake Check"
    )

    //    String [] serviceID = {"1","2","3","4","5","6","7","8","9","10","11"};
    //variables to hold the input data
    var v_providerName: String? = null
    var v_providerPassWord: String? = null
    var v_email: String? = null
    var v_contact: String? = null
    var v_address: String? = null
    var v_city: String? = null
    var emailIsValidated = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provider_registration)

        //EditText - Button
        val name = findViewById<EditText>(R.id.ServiceProviderName)
        val passWord = findViewById<EditText>(R.id.ServiceProviderPassword)
        val email = findViewById<EditText>(R.id.ServiceProviderEmail)
        val contact = findViewById<EditText>(R.id.ServiceProviderContact)
        val address = findViewById<EditText>(R.id.ServiceProviderAddress)
        val city = findViewById<EditText>(R.id.ServiceProviderCity)
        val btnProviderRegister = findViewById<Button>(R.id.btnProviderRegister)

        //initial all views
        providerServices = findViewById(R.id.ProviderServices)
        selectedServices = BooleanArray(serviceProvide.size)
        //Checkboxes for services drop_down
        providerServices.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                showServiceDialog()
            }

            private fun showServiceDialog() {
                val builder = AlertDialog.Builder(this@ProviderRegistration)
                builder.setCancelable(false)
                builder.setMultiChoiceItems(
                    serviceProvide,
                    selectedServices
                ) { dialog, which, isChecked ->
                    if (isChecked) {
                        selectedLocation.add(which)
                    } else selectedLocation.removeAt(which)
                }
                    .setPositiveButton("Ok") { dialog, which ->
                        for (i in selectedLocation.indices) {
                            //create String builder
                            val stringBuilder = StringBuilder()
                            stringBuilder.append(serviceProvide[selectedLocation[i]])
                            selectedServiceList.add(stringBuilder)
                        }
                    }
                    .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
                    .setNeutralButton("Clear all") { dialog, which ->
                        for (i in selectedServices.indices) {
                            selectedServices[i] = false
                            selectedServiceList.clear()
                        }
                    }
                builder.show()
            }
        })

        //On click listener to hold input values
        btnProviderRegister.setOnClickListener {
            v_providerName = name.text.toString()
            v_providerPassWord = passWord.text.toString()
            v_email = email.text.toString()
            v_contact = contact.text.toString()
            v_address = address.text.toString()
            v_city = city.text.toString()
            emailIsValidated = validateEmail(v_email!!)
            //validate input
            if (v_providerName!!.isEmpty() || v_providerPassWord!!.isEmpty() || v_email!!.isEmpty() ||
                v_contact!!.isEmpty() || v_address!!.isEmpty() || v_city!!.isEmpty()
            ) {
                Toast.makeText(
                    applicationContext,
                    "Please provide all the required fields!",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (emailIsValidated == true) {
                Toast.makeText(
                    this@ProviderRegistration,
                    "Email is already existing",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                //add service provider data
                dbHelper.insertServiceProvider(
                    v_providerPassWord, v_providerName, v_address, v_city, null, null, v_email,
                    v_contact, "d"
                )

                //get service provider ID from service provider email
                var serviceProviderID: Int? = null
                val cursor = dbHelper.getServiceProviderID(v_email!!)
                if (cursor!!.moveToFirst()) {
                    serviceProviderID =
                        cursor.getInt(cursor.getColumnIndexOrThrow("ServiceProviderID"))
                }

                //insert service list to SERVICE_LIST table
                var serviceListID = ""
                var serviceDetailID: Int
                for (i in selectedLocation.indices) {
                    serviceDetailID = selectedLocation[i] + 1
                    serviceListID = "SP_" + serviceProviderID + "_" + serviceDetailID
                    dbHelper.insertServiceList(
                        serviceListID, serviceProviderID!!, serviceDetailID
                    )
                }
                Toast.makeText(
                    this@ProviderRegistration,
                    "Successful registration!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            //remove all items in arrayList everytime
            selectedServiceList.clear()
        }

        //Already have an account  - Login activity
        val logIn = findViewById<TextView>(R.id.logInHere)
        logIn.setOnClickListener {
            val intent = Intent(this@ProviderRegistration, LogIn::class.java)
            startActivity(intent)
        }
    }

    //Validate register email
    fun validateEmail(email: String): Boolean {
        var result = false
        val cursor = dbHelper.serviceProviderInfo
        try {
            if (cursor!!.count > 0) {
                cursor.moveToPosition(-1)
                while (cursor.moveToNext()) {
                    if (cursor.getString(cursor.getColumnIndexOrThrow("email"))
                            .trim { it <= ' ' } == email
                    ) {
                        result = true
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return result
    }
}
