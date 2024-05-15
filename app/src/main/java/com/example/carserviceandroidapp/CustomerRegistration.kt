package com.example.carserviceandroidapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CustomerRegistration : AppCompatActivity() {
    var dbh = DBHelper(this)
    var etName: EditText? = null
    var etPW: EditText? = null
    var etConfirmPW: EditText? = null
    var etEmail: EditText? = null
    var etAddress: EditText? = null
    var etContact: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_registration)
        etName = findViewById<View>(R.id.ProviderName) as EditText
        etPW = findViewById<View>(R.id.ProviderPassWord) as EditText
        etConfirmPW = findViewById<View>(R.id.ProviderConfirmPassword) as EditText
        etEmail = findViewById<View>(R.id.editTextEmail) as EditText
        etAddress = findViewById<View>(R.id.ProviderAddress) as EditText
        etContact = findViewById<View>(R.id.ProviderContact) as EditText
        val buttonRegister = findViewById<View>(R.id.bthCustomerRegister) as Button
        buttonRegister.setOnClickListener {
            val cName = etName!!.text.toString()
            val cPW = etPW!!.text.toString()
            val confPW = etConfirmPW!!.text.toString()
            val cEmail = etEmail!!.text.toString()
            val cNumber = etContact!!.text.toString()
            val cAddress = etAddress!!.text.toString()
            val emailIsValidated = validateEmail(cEmail)
            val passwordCheck = comparedPassword(cPW, confPW)
            val checkInputs = validateInfo(cName, cPW, confPW, cEmail, cAddress, cNumber)
            if (checkInputs == true && emailIsValidated == false && passwordCheck == true) {
                val customer_account = CustomerAccount(cName, cPW, cEmail, cNumber, cAddress)
                dbh.insertuserdata(
                    customer_account.getcName(), customer_account.getcPassword(),
                    customer_account.getcEmail(), customer_account.getcMobile(),
                    customer_account.getcAddress()
                )
                val alert = AlertDialog.Builder(this@CustomerRegistration)
                val mView = layoutInflater.inflate(R.layout.customer_registration_dialogue, null)
                alert.setView(mView)
                val alertDialog = alert.create()
                alertDialog.setCancelable(false)
                mView.findViewById<View>(R.id.okBTNReg).setOnClickListener { view: View? ->
                    // Toast.makeText(this, "Clicked OK BTN", Toast.LENGTH_SHORT).show();
                    val intent = Intent(this@CustomerRegistration, LogIn::class.java)
                    startActivity(intent)
                    alertDialog.dismiss()
                }
                alertDialog.show()
            } else {
                if (emailIsValidated == true) {
                    Toast.makeText(
                        this@CustomerRegistration,
                        "Email is already existing",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (passwordCheck == false) {
                    Toast.makeText(
                        this@CustomerRegistration,
                        "Password Did Not Match",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (emailIsValidated == false && passwordCheck == false) {
                    Toast.makeText(
                        this@CustomerRegistration,
                        "Email is already registered and password is incorrect.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this@CustomerRegistration,
                        "Some fields do not meet the needed parameters.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        var result = false
        val cursor = dbh.customerData
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

    private fun validateInfo(
        cName: String,
        pWord: String,
        confPW: String,
        cEmail: String,
        cAddress: String,
        cContact: String
    ): Boolean {
        return if (cName.length == 0) {
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

    fun comparedPassword(cPW: String, confPW: String): Boolean {
        val passwordOk = false
        return if (cPW == confPW) {
            true
        } else {
            false
        }
    }

    //temporary. to replace accordingly later
    fun onClickToAppointmentsView(v: View?) {
        val intent = Intent(this@CustomerRegistration, LogIn::class.java)
        startActivity(intent)
    } //    public void onClickToHistoryView(View v){
    //        Intent intent = new Intent(Customer_Registration.this, Customer_ServiceHistoryView.class);
    //        startActivity(intent);
    //    }
}