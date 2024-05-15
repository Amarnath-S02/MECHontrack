package com.example.carserviceandroidapp

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carserviceandroidapp.adapter.CustomAdapter
import com.example.carserviceandroidapp.adapter.CustomAdapter.ItemClickListener

class CustomerFindServiceProviderList() : AppCompatActivity(), ItemClickListener {
    var DB: DBHelper? = null
    var adapter: CustomAdapter? = null
    var location: String? = ""
    var spID: String? = null
    var fullLoc: String? = null

    //arraylist to store the image and the text for the recycle views
    var aList: ArrayList<ImageAndText> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_find_service_provider_list)
        DB = DBHelper(this)
        val intent: Intent? = getIntent()
        val txtServiceLoc: TextView = findViewById(R.id.txtServiceLocation)
        if (intent != null) {
            location = intent.getStringExtra("LOC")
            txtServiceLoc.setText("Available Service Providers at \n" + location)
        }
        //displaying the image and the text name and address of the service providers
        displayData()
        //showing the recycle view by 2 columns
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val numOfCols: Int = 2
        recyclerView.setLayoutManager(GridLayoutManager(this, numOfCols))
        adapter = CustomAdapter(this, aList, this)
        recyclerView.setAdapter(adapter)
    }

    public override fun onItemClick(view: View?, position: Int) {
        spID = aList.get(position).serviceProviderID
        val intent: Intent =
            Intent(this@CustomerFindServiceProviderList, CustomerScheduleDropOff::class.java)
        intent.putExtra("SPID", spID)
        startActivity(intent)
    }

    //displaying the image and the text name and address of the service providers
    private fun displayData() {
        val cursor: Cursor? = DB?.serviceProviderList
        if (cursor!!.getCount() == 0) {
            Toast.makeText(
                this@CustomerFindServiceProviderList,
                "No Entry Exists",
                Toast.LENGTH_SHORT
            ).show()
            return
        } else {
            while (cursor.moveToNext()) {
                if ((cursor.getString(4) == location)) {
                    val imageName: String =
                        cursor.getString(9) // replace with the name of the desired image
                    val imageResourceId: Int =
                        getResources().getIdentifier(imageName, "drawable", getPackageName())
                    fullLoc = (cursor.getString(2) + "\n" + cursor.getString(3) + "," +
                        cursor.getString(4) + ", " + cursor.getString(5) + ", "
                        + cursor.getString(6))
                    aList.add(ImageAndText(fullLoc!!, imageResourceId, cursor.getString(0)))
                }
            }
        }
    }
}