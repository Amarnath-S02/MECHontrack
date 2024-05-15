package com.example.carserviceandroidapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    var DB: DBHelper? = null
    lateinit var toKen: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DB = DBHelper(this)
        val btnDummyData = findViewById<Button>(R.id.btnDummyData)
        toKen = findViewById(R.id.toKenneth)
        val generatePDF = findViewById<Button>(R.id.createPDFbtn)
        generatePDF.setOnClickListener { generatePDF() }
        toKen.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@MainActivity, LogIn::class.java)
            startActivity(intent)
        })
    }

    fun onClickToGilbert(v: View?) {
        val intent = Intent(this@MainActivity, CustomerRegistration::class.java)
        startActivity(intent)
    }

    private fun generatePDF() {
        DB!!.insertuserdata(
            "Kenneth",
            "password1",
            "kenleano@gmail.com",
            "9876543210",
            "123 Main St"
        )
    }
}