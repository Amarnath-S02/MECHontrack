package com.example.carserviceandroidapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class ServiceMainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_main_menu)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener(navListener)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ServiceAccount())
            .commit()
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFragment: Fragment? = null
        when (item.itemId) {
            R.id.account -> selectedFragment = ServiceAccount()
            R.id.appointment -> selectedFragment = ProviderAppointment()
            R.id.history -> selectedFragment = ServiceHistory()
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, selectedFragment!!).commit()
        true
    }
}