package com.example.carserviceandroidapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.Random

class ServiceAccount : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_account, container, false)
        val editProfile = rootView.findViewById<Button>(R.id.editProfileBtn)
        val profileName = rootView.findViewById<TextView>(R.id.profileName)
        val imageProfile = rootView.findViewById<ImageView>(R.id.imageView6)

        //Retrieves Service Provider Name from DBHelper
        val db = DBHelper(activity)
        val spID = ServiceProvider.ServiceProviderID
        val cID = Customer.CustomerID
        if (spID > 0) {
            val cursor = db.getServiceProviderName(spID)
            cursor!!.moveToFirst()
            val imageCursor = db.getServiceProviderImage(spID)
            imageCursor!!.moveToFirst()
            val imageLetter = imageCursor.getString(0)
            var image = 0
            when (imageLetter) {
                "a" -> image = com.example.gark.R.drawable.a
                "b" -> image = com.example.gark.R.drawable.b
                "c" -> image = com.example.gark.R.drawable.c
                "d" -> image = com.example.gark.R.drawable.d
                "e" -> image = com.example.gark.R.drawable.e
                "f" -> image = com.example.gark.R.drawable.f
                "g" -> image = com.example.gark.R.drawable.g
                "h" -> image = com.example.gark.R.drawable.h
            }
            profileName.text = cursor.getString(0)
            imageProfile.setImageResource(image)
        }
        if (cID > 0) {
            val custCursor = db.getCustomerName(cID)
            custCursor!!.moveToFirst()
            val imageCursor = db.getServiceProviderImage(spID)
            imageCursor!!.moveToFirst()
            val random = Random()
            val randomChar = (random.nextInt(8) + 'a'.code).toChar()
            var image = 0
            when (randomChar) {
                'a' -> image = R.drawable.c1
                'b' -> image = R.drawable.c2
                'c' -> image = R.drawable.c3
                'd' -> image = R.drawable.c4
                'e' -> image = R.drawable.c5
                'f' -> image = R.drawable.c6
                'g' -> image = R.drawable.c7
                'h' -> image = R.drawable.c8
            }
            profileName.text = custCursor.getString(0)
            imageProfile.setImageResource(image)
        }
        val logOut = rootView.findViewById<Button>(R.id.logOut)
        //Logs out user, clearing global ID variables.
        logOut.setOnClickListener {
            Customer.CustomerID = 0
            ServiceProvider.ServiceProviderID = 0
            Log.d("ServiceProviderID LOG:", ServiceProvider.ServiceProviderID.toString())
            Log.d("ServiceProviderID LOG:", Customer.CustomerID.toString())
            startActivity(Intent(rootView.context, LogIn::class.java))
        }

        //Starts EditProfileActivity for Service Provider
        editProfile.setOnClickListener {
            if (ServiceProvider.ServiceProviderID == 0) {
                startActivity(Intent(rootView.context, CustomerEditProfile::class.java))
            } else {
                startActivity(Intent(rootView.context, ProviderEditProfile::class.java))
            }
        }
        return rootView
    }
}
