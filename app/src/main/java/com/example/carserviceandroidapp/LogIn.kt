package com.example.carserviceandroidapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LogIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        val username = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.passwordTxt)
        val login = findViewById<Button>(R.id.sendLink)
        //LOGIN BUTTON
        login.setOnClickListener {
            val enteredUsername = username.text.toString()
            val enteredPassword = password.text.toString()
            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                // show error if either field is empty
                Toast.makeText(
                    applicationContext,
                    "Username or password is empty",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val dbHelper = DBHelper(this@LogIn)

                //Checks if login details are correct.
                // Returns CUSTOMER, SERVICE_PROVIDED, or NOT_FOUND on loginStatus[0]
                //Returns CustomerID or Service ID on loginStatus[1]
                val loginStatus = dbHelper.checkLoginID(enteredUsername, enteredPassword)
                if (loginStatus!![0] == "CUSTOMER") {


                    //Stores ID to global variable
                    Customer.CustomerID = loginStatus[1]!!.toInt()
                    Log.d("CustomerID LOG:", Customer.CustomerID.toString())
                    // login customer successful, start app Customer Module
                    val intent = Intent(this@LogIn, CustomerMainMenu::class.java)
                    startActivity(intent)
                } else if (loginStatus[0] == "SERVICE_PROVIDER") {


                    //Stores ID to global variable
                    ServiceProvider.ServiceProviderID = loginStatus[1]!!
                        .toInt()
                    Log.d("ServiceProviderID LOG:", ServiceProvider.ServiceProviderID.toString())
                    // login service provider successful, start app Service Provider Module
                    val intent = Intent(this@LogIn, ServiceMainMenu::class.java)
                    startActivity(intent)
                } else if (loginStatus[0] == "NOT_FOUND") {
                    //login details not in Database, show error
                    Toast.makeText(
                        applicationContext,
                        "Incorrect email or password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        //REGISTER AS SERVICE PROVIDER
        val regServiceTxt = findViewById<TextView>(R.id.regService)
        regServiceTxt.setOnClickListener {
            val intent = Intent(this@LogIn, ProviderRegistration::class.java)
            startActivity(intent)
        }
        //REGISTER AS CUSTOMER
        val regCustomerTxt = findViewById<TextView>(R.id.regCustomer)
        regCustomerTxt.setOnClickListener {
            val intent = Intent(this@LogIn, CustomerRegistration::class.java)
            startActivity(intent)
        }
        //Forgot Password
        val forgotPw = findViewById<TextView>(R.id.forgotPassword)
        forgotPw.setOnClickListener {
            val intent = Intent(this@LogIn, ForgotPassword::class.java)
            startActivity(intent)
        }
    }
}