package com.example.carserviceandroidapp

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProviderEditProfile : AppCompatActivity() {
    var dbHelper: DBHelper? = null
    var spID: Int? = null
    var editName: String? = null
    var editPassword: String? = null
    var editEmail: String? = null
    var editContact: String? = null
    var editAddress: String? = null
    var editCity: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provider_edit_profile)
        dbHelper = DBHelper(this)
        val name = findViewById<TextView>(R.id.editProviderName)
        val password = findViewById<EditText>(R.id.editProviderPassword)
        val email = findViewById<TextView>(R.id.editProviderEmail)
        val contact = findViewById<EditText>(R.id.editProviderContact)
        val address = findViewById<EditText>(R.id.editProviderAddress)
        val city = findViewById<EditText>(R.id.editProviderCity)
        val buttonSaveChanges = findViewById<Button>(R.id.btnSaveChanges)
        val buttonDeleteChanges = findViewById<Button>(R.id.btnDeleteAccount)
        spID = ServiceProvider.ServiceProviderID
        displayService()
        name.text = editName
        email.text = editEmail
        password.setText(editPassword)
        city.setText(editCity)
        contact.setText(editContact)
        address.setText(editAddress)
        buttonDeleteChanges.setOnClickListener {
            val builder = AlertDialog.Builder(this@ProviderEditProfile)
            builder.setTitle("Warning")
            builder.setMessage("Do your really want to delete your account?")
            builder.setPositiveButton("Confirm") { dialog, which -> // Action to take when "Confirm" is clicked
                val deletedata = dbHelper!!.deleteServiceProvider(spID!!)
                if (deletedata == true) {
                    Toast.makeText(
                        this@ProviderEditProfile,
                        "Deleted successfully",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(this@ProviderEditProfile, "Failed to delete", Toast.LENGTH_LONG)
                        .show()
                }
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
                // Action to take when "Cancel" is clicked
            }
            builder.show()
        }
        buttonSaveChanges.setOnClickListener {
            val checkupdatedata = dbHelper!!.updateServiceProviderProfile(
                spID,
                password.text.toString(),
                name.text.toString(),
                address.text.toString(),
                city.text.toString(),
                contact.text.toString(),
                email.text.toString()
            )
            if (checkupdatedata == true) {
                Toast.makeText(this@ProviderEditProfile, "Updated Successfully", Toast.LENGTH_LONG)
                    .show()
            } else {
                Toast.makeText(this@ProviderEditProfile, "Check input fields", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun displayService() {
        val cursor = dbHelper?.serviceProviderDataAll
        if (cursor!!.count == 0) {
            Toast.makeText(this@ProviderEditProfile, "No Entry Exists", Toast.LENGTH_SHORT).show()
            return
        } else {
            while (cursor!!.moveToNext()) {
                if (cursor!!.getString(0).toInt() == spID) {
                    editName = cursor!!.getString(2)
                    editPassword = cursor!!.getString(1)
                    editEmail = cursor!!.getString(8)
                    editContact = cursor!!.getString(7)
                    editAddress = cursor!!.getString(3)
                    editCity = cursor!!.getString(4)
                }
            }
        }
    }
}