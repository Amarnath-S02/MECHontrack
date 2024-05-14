package com.example.carserviceandroidapp

import android.app.blob.BlobStoreManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
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

class ForgotPassword : AppCompatActivity() {
    var userPassword = ""
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        //EditText emailTxt = findViewById(R.id.email);
        val sendLink: Button = findViewById<Button>(R.id.sendLink)
        sendLink.setOnClickListener(View.OnClickListener {
            val emailEditText: EditText = findViewById<EditText>(R.id.email)
            val recipient: String = emailEditText.getText().toString()

            //checks if username or password is empty and creates toast
            val dbHelper = DBHelper(this@ForgotPassword)
            //Checks if login details are correct.
            // Returns CUSTOMER, SERVICE_PROVIDED, or NOT_FOUND on loginStatus[0]
            //Returns CustomerID or Service ID on loginStatus[1]
            //Returns password on loginStatus[2]
            val loginStatus = dbHelper.checkEmail(recipient)
            if (recipient.isEmpty()) {
                Toast.makeText(
                    getApplicationContext(),
                    "Please enter your email",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            } else if (loginStatus!![0] == "NOT_FOUND") {
                Toast.makeText(getApplicationContext(), "Email does not exist", Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            } else if (loginStatus[0] == "CUSTOMER") {
                Toast.makeText(getApplicationContext(), "Email Sent!", Toast.LENGTH_LONG).show()
                Customer.CustomerID = loginStatus[1]!!.toInt()
                userPassword = loginStatus[2].toString()
                sendEmail(recipient)
                Log.d("CustomerID LOG:", Customer.CustomerID.toString())
            } else if (loginStatus[0] == "SERVICE_PROVIDER") {
                Toast.makeText(getApplicationContext(), "Email Sent!", Toast.LENGTH_LONG).show()
                ServiceProvider.ServiceProviderID = loginStatus[1]!!
                    .toInt()
                userPassword = loginStatus[2].toString()
                sendEmail(recipient)
                Log.d("ServiceProviderID LOG:", ServiceProvider.ServiceProviderID.toString())
            }
        })
    }

    private fun sendEmail(emailEditText: String) {
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
                val message: Message = MimeMessage(session)
                message.setFrom(InternetAddress(username))
                message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(emailEditText)
                )
                message.setSubject("Gark App Forgot Password")
                message.setText(
                    "Greetings! " + "\n\nIt looks like you forgot something. Your password is: " + userPassword +
                        " \n\n Cheers!\n"
                )
                Transport.send(message)
                Log.i(TAG, "Email sent successfully")
                userPassword = ""
            } catch (e: MessagingException) {
                Log.e(TAG, "Email sending failed: " + e.message)
            }
        }.start()
    }

    companion object {
        private const val TAG = "ForgotPassword"
    }
}