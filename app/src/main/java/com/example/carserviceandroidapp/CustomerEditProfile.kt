package com.example.carserviceandroidapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CustomerEditProfile : AppCompatActivity() {
    var DB: DBHelper? = null
    var username: String? = null
    var password: String? = null
    var confirm_password: String? = null
    var email: String? = null
    var prevemail: String? = null
    var mobile: String? = null
    var address: String? = null
    var userID = 0
    var etName: EditText? = null
    var etPW: EditText? = null
    var etConfirmPW: EditText? = null
    var etEmail: EditText? = null
    var etAddress: EditText? = null
    var etContact: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_edit_profile)
        DB = DBHelper(this)
        val txtCustName = findViewById<TextView>(R.id.txtCustName)
        val editTxtUserName = findViewById<EditText>(R.id.editTxtUserName)
        val editTxtEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTxtPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTxtConfirmPassword = findViewById<EditText>(R.id.editTextConfirmPassword)
        val editTxtMobile = findViewById<EditText>(R.id.editTextMobile)
        val editTxtAddress = findViewById<EditText>(R.id.editTxtAddress)
        val buttonSaveChanges = findViewById<Button>(R.id.btnSaveChanges)
        val buttonDeleteChanges = findViewById<Button>(R.id.btnDeleteCust)
        etName = editTxtUserName
        etPW = editTxtPassword
        etConfirmPW = editTxtConfirmPassword
        etEmail = editTxtEmail
        etAddress = editTxtAddress
        etContact = editTxtMobile
        userID = Customer.CustomerID
        displaydata() // display all the information of the customer
        txtCustName.text = username
        editTxtUserName.setText(username)
        editTxtEmail.setText(email)
        editTxtPassword.setText(password)
        editTxtConfirmPassword.setText(password)
        editTxtMobile.setText(mobile)
        editTxtAddress.setText(address)
        buttonDeleteChanges.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                ShowDialogBox()
            }

            //method to show the confirmation dialog to delete an account
            private fun ShowDialogBox() {
                val alert = AlertDialog.Builder(this@CustomerEditProfile)
                val mView = layoutInflater.inflate(R.layout.confirmation_dialog, null)
                alert.setView(mView)
                val alertDialog = alert.create()
                alertDialog.setCancelable(false)
                mView.findViewById<View>(R.id.chancelBTN)
                    .setOnClickListener { v: View? -> alertDialog.dismiss() }
                mView.findViewById<View>(R.id.okBTN).setOnClickListener { v: View? ->
                    val dateFormat: DateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                    val calendar = Calendar.getInstance()
                    val currentDate = calendar.time
                    val CurrDate = dateFormat.format(currentDate)
                    val checkDelData = DB!!.cancelApt(userID, "Cancelled", CurrDate)
                    val deletedata = DB!!.deleteData(userID)
                    if (deletedata == true && checkDelData == true) {
                        showDelDialog()
                        //Toast.makeText(CustomerEditProfile.this, "Deleted successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(
                            this@CustomerEditProfile,
                            "Failed to delete",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    alertDialog.dismiss()
                }
                alertDialog.show()
            }

            //method to show if an account has been successfully deleted
            private fun showDelDialog() {
                val alert = AlertDialog.Builder(this@CustomerEditProfile)
                val mView = layoutInflater.inflate(R.layout.confirm_delete_dialog, null)
                alert.setView(mView)
                val alertDialog = alert.create()
                alertDialog.setCancelable(false)
                mView.findViewById<View>(R.id.okBTN).setOnClickListener { v: View? ->
                    val intent = Intent(this@CustomerEditProfile, LogIn::class.java)
                    startActivity(intent)
                    alertDialog.dismiss()
                }
                alertDialog.show()
            }
        })
        buttonSaveChanges.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val cName = editTxtUserName.text.toString()
                val cPW = editTxtPassword.text.toString()
                val confPW = editTxtConfirmPassword.text.toString()
                val cEmail = editTxtEmail.text.toString()
                val cAddress = editTxtAddress.text.toString()
                val cNumber = editTxtMobile.text.toString()
                val emailIsValidated = validateEmail(cEmail)
                val passwordCheck = comparedPassword(cPW, confPW)
                val checkInputs = validateInfo(cName, cPW, confPW, cEmail, cAddress, cNumber)
                if (checkInputs == true && emailIsValidated == false && passwordCheck == true) {
                    showDialogBox2()
                } else {
                    if (emailIsValidated == true) {
                        Toast.makeText(
                            this@CustomerEditProfile,
                            "Email is already existing",
                            Toast.LENGTH_LONG
                        ).show()
                    } else if (passwordCheck == false) {
                        Toast.makeText(
                            this@CustomerEditProfile,
                            "Password Did Not Match",
                            Toast.LENGTH_LONG
                        ).show()
                    } else if (emailIsValidated == false && passwordCheck == false) {
                        Toast.makeText(
                            this@CustomerEditProfile,
                            "Email is already registered and password is incorrect.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this@CustomerEditProfile,
                            "Some fields do not meet the needed parameters.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            //method to show confirmation if update is successful
            private fun showDialogBox2() {
                val alert = AlertDialog.Builder(this@CustomerEditProfile)
                val mView = layoutInflater.inflate(R.layout.confirm_customer_update_dialog, null)
                alert.setView(mView)
                val alertDialog = alert.create()
                alertDialog.setCancelable(false)
                val checkupdatedata = DB!!.updateData(
                    editTxtUserName.text.toString(),
                    userID,
                    editTxtPassword.text.toString(),
                    editTxtEmail.text.toString(),
                    editTxtMobile.text.toString(),
                    editTxtAddress.text.toString()
                )
                if (checkupdatedata == true) {
                    // Toast.makeText(CustomerEditProfile.this, "Updated Successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this@CustomerEditProfile, "Failed to Updated", Toast.LENGTH_LONG)
                        .show()
                }
                mView.findViewById<View>(R.id.okBTN).setOnClickListener { v: View? ->
                    val intent = Intent(this@CustomerEditProfile, CustomerMainMenu::class.java)
                    startActivity(intent)
                    alertDialog.dismiss()
                }
                alertDialog.show()
            }
        })
    }

    //validation for all the fields information
    private fun validateInfo(
        cName: String,
        pWord: String,
        confPW: String,
        cEmail: String,
        cAddress: String,
        cContact: String
    ): Boolean {
        return if (cName.isEmpty()) {
            etName!!.requestFocus()
            etName!!.error = "FIELD CANNOT BE EMPTY"
            false
        } else if (!cName.matches(("(?=.*[a-zA-Z])" + ".{4,}").toRegex())) {   //any letter,  at least 4
            etName!!.requestFocus()
            etName!!.error = "ENTER AT LEAST 4 CHARACTERS"
            false
        } else if (pWord.length == 0) {
            etPW!!.requestFocus()
            etPW!!.error = "FIELD CANNOT BE EMPTY"
            false
        } else if (confPW.length == 0) {
            etConfirmPW!!.requestFocus()
            etConfirmPW!!.error = "FIELD CANNOT BE EMPTY"
            false
        } else if (cEmail.length == 0) {
            etEmail!!.requestFocus()
            etEmail!!.error = "FIELD CANNOT BE EMPTY"
            false
        } else if (cAddress.length == 0) {
            etAddress!!.requestFocus()
            etAddress!!.error = "FIELD CANNOT BE EMPTY"
            false
        } else if (cContact.length == 0) {
            etContact!!.requestFocus()
            etContact!!.error = "FIELD CANNOT BE EMPTY"
            false
        } else if (!cEmail.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex())) {
            etEmail!!.requestFocus()
            etEmail!!.error = "ENTER VALID EMAIL"
            false
        } else if (pWord.length == 0) {
            etPW!!.requestFocus()
            etPW!!.error = "FIELD CANNOT BE EMPTY"
            false
        } else {
            true
        }
    }

    //email validation
    private fun validateEmail(email: String): Boolean {
        var result = false
        val cursor = DB?.customerData
        try {
            if (cursor!!.count > 0) {
                cursor!!.moveToPosition(-1)
                while (cursor!!.moveToNext()) {
                    if (cursor!!.getString(cursor!!.getColumnIndexOrThrow("email"))
                            .trim { it <= ' ' } == email && prevemail != email
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

    //password validation
    fun comparedPassword(cPW: String, confPW: String): Boolean {
        val passwordOk = false
        return if (cPW == confPW) {
            true
        } else {
            false
        }
    }

    //display all the data in the fields information
    private fun displaydata() {
        val cursor = DB?.customerData
        if (cursor!!.count == 0) {
            Toast.makeText(this@CustomerEditProfile, "No Entry Exists", Toast.LENGTH_SHORT).show()
            return
        } else {
            while (cursor!!.moveToNext()) {
                if (cursor!!.getString(0).toInt() == userID) {
                    username = cursor!!.getString(1)
                    password = cursor!!.getString(2)
                    email = cursor!!.getString(3)
                    prevemail = cursor!!.getString(3)
                    mobile = cursor!!.getString(4)
                    address = cursor!!.getString(5)
                }
            }
        }
    }
}