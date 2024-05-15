package com.example.carserviceandroidapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class PlainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plain)
        val customer_appointmentsView = CustomerAppointmentsView()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.plainLayout, customer_appointmentsView)
        transaction.commit()
        val homeButton = findViewById<View>(R.id.buttonHomePlain) as Button
        homeButton.setOnClickListener {
            startActivity(
                Intent(
                    this@PlainActivity,
                    CustomerMainMenu::class.java
                )
            )
        }
    }
}