package com.example.carserviceandroidapp

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment

class CustomerFindServiceProviderLocation() : Fragment() {
    var location: ArrayList<String> = ArrayList()
    var spinLoc: String? = null
    var DB: DBHelper? = null
    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val view: View = inflater.inflate(
            R.layout.activity_customer_find_service_provider_location,
            container,
            false
        )
        val context: Context = getContext()!!.getApplicationContext()
        DB = DBHelper(getActivity())
        displaydata()

        //spinner to choose the location
        val Spinner: Spinner = view.findViewById(R.id.spinLocation)
        val btnNextLocation: Button = view.findViewById(R.id.btnNextLocation)
        btnNextLocation.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                val intent: Intent =
                    Intent(getActivity(), CustomerFindServiceProviderList::class.java)
                intent.putExtra("LOC", spinLoc)
                startActivity(intent)
            }
        })
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            (getActivity())!!,
            android.R.layout.simple_spinner_item, location
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        Spinner.setAdapter(adapter)
        Spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            public override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedLocation: String = parent.getItemAtPosition(position).toString()
                spinLoc = selectedLocation
            }

            public override fun onNothingSelected(parent: AdapterView<*>?) {
                spinLoc = location.get(0)
            }
        })
        return view
    }

    //method to display the list location of the providers
    private fun displaydata() {
        val cursor: Cursor? = DB?.serviceProviderData
        if (cursor!!.getCount() == 0) {
            Toast.makeText(getActivity(), "No Entry Exists", Toast.LENGTH_SHORT).show()
            return
        } else {
            while (cursor.moveToNext()) {
                location.add(cursor.getString(0))
            }
        }
    }
}