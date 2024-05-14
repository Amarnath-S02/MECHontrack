package com.example.carserviceandroidapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ServiceHistory : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_service_history, container, false)
        val context = context!!.applicationContext
        val historyItems: MutableList<ServiceHistoryItems> = ArrayList()
        val filterSpinner = view.findViewById<Spinner>(R.id.filterSpinner)
        val recyclerView = view.findViewById<RecyclerView>(R.id.historyRecyclerView)
        val generateReportButton = view.findViewById<Button>(R.id.generateReportButton)
        ActivityCompat.requestPermissions(
            activity!!, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), PackageManager.PERMISSION_GRANTED
        )
        generateReportButton.setOnClickListener {
            val filteredItems: MutableList<ServiceHistoryItems> = ArrayList()
            // Filter the data based on the selected spinner option
            val selectedOption = filterSpinner.selectedItem.toString()
            if (selectedOption == "Completed") {
                for (item in historyItems) {
                    if (item.serviceAppointmentStatus == "Completed") {
                        filteredItems.add(item)
                    }
                }
            } else if (selectedOption == "Cancelled") {
                for (item in historyItems) {
                    if (item.serviceAppointmentStatus == "Cancelled") {
                        filteredItems.add(item)
                    }
                }
            } else {
                filteredItems.addAll(historyItems)
            }
            // Create a PDF report based on the filtered data
            generatePDF(filteredItems, recyclerView)
        }
        filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedOption = parent.getItemAtPosition(position).toString()
                val filteredItems: MutableList<ServiceHistoryItems> = ArrayList()
                if (selectedOption == "Completed") {
                    for (item in historyItems) {
                        if (item.serviceAppointmentStatus == "Completed") {
                            filteredItems.add(item)
                        }
                    }
                } else if (selectedOption == "Cancelled") {
                    for (item in historyItems) {
                        if (item.serviceAppointmentStatus == "Cancelled") {
                            filteredItems.add(item)
                        }
                    }
                } else {
                    filteredItems.addAll(historyItems)
                }
                recyclerView.adapter = ServiceHistoryAdapter(activity, filteredItems)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
        val serviceProviderID = ServiceProvider.ServiceProviderID
        val dbHelper = DBHelper(activity)
        val db = dbHelper.readableDatabase
        db.rawQuery(QUERY_COMPLETED_APPOINTMENTS, arrayOf(serviceProviderID.toString()))
            .use { cursor ->
                while (cursor.moveToNext()) {
                    val appointmentID =
                        cursor.getString(cursor.getColumnIndexOrThrow("AppointmentID"))
                    val customerName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    val customerNumber = cursor.getString(cursor.getColumnIndexOrThrow("mobile"))
                    val customerEmail = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                    val pickUpDateTime =
                        cursor.getString(cursor.getColumnIndexOrThrow("PickUpDateTime"))
                    val pickUpReadyDate =
                        cursor.getString(cursor.getColumnIndexOrThrow("PickUpReadyDate"))
                    val dropOffTimeDate =
                        cursor.getString(cursor.getColumnIndexOrThrow("DropOffTimeDate"))
                    val appointmentStatus =
                        cursor.getString(cursor.getColumnIndexOrThrow("AppointmentStatus"))
                    val appointmentType =
                        cursor.getString(cursor.getColumnIndexOrThrow("AppointmentType"))
                    val serviceName = cursor.getString(cursor.getColumnIndexOrThrow("ServiceName"))
                    historyItems.add(
                        ServiceHistoryItems(
                            appointmentID,
                            customerName,
                            customerNumber,
                            customerEmail,
                            pickUpDateTime,
                            pickUpReadyDate,
                            dropOffTimeDate,
                            appointmentStatus,
                            serviceName,
                            appointmentType
                        )
                    )
                }
            }
        db.rawQuery(QUERY_CANCELLED_APPOINTMENTS, arrayOf(serviceProviderID.toString()))
            .use { cursor ->
                while (cursor.moveToNext()) {
                    val appointmentID =
                        cursor.getString(cursor.getColumnIndexOrThrow("AppointmentID"))
                    val customerName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    val customerNumber = cursor.getString(cursor.getColumnIndexOrThrow("mobile"))
                    val customerEmail = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                    val dropOffTimeDate =
                        cursor.getString(cursor.getColumnIndexOrThrow("DropOffTimeDate"))
                    val appointmentStatus =
                        cursor.getString(cursor.getColumnIndexOrThrow("AppointmentStatus"))
                    val appointmentType =
                        cursor.getString(cursor.getColumnIndexOrThrow("AppointmentType"))
                    val serviceName = cursor.getString(cursor.getColumnIndexOrThrow("ServiceName"))
                    historyItems.add(
                        ServiceHistoryItems(
                            appointmentID,
                            customerName,
                            customerNumber,
                            customerEmail,
                            appointmentStatus,
                            serviceName,
                            dropOffTimeDate,
                            appointmentType
                        )
                    )
                }
            }
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = ServiceHistoryAdapter(activity, historyItems)
        return view
    }

    private fun generatePDF(filteredItems: List<ServiceHistoryItems>, recyclerView: RecyclerView) {
        // Define the name of the PDF file
        val pdfFileName =
            "Service_Report-" + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(
                Date()
            ) + ".pdf"

        // Get a directory where the PDF will be saved
        val pdfFileDirectory = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                .toString() + "/" + pdfFileName
        )
        try {
            // Create a new document object
            val document = PdfDocument()

            // Create a page description
            val pageInfo =
                PageInfo.Builder(recyclerView.width, recyclerView.measuredHeight + 2000, 1).create()

            // Start a new page
            val page = document.startPage(pageInfo)
            recyclerView.measure(
                View.MeasureSpec.makeMeasureSpec(recyclerView.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )

            // Get a canvas to draw on the page
            val canvas = page.canvas

            // Create a paint object for text
            val paint = Paint()
            paint.color = Color.BLACK
            paint.textSize = 96f

            // Draw the "Service Provider Report" text


            // Load the logo drawable
            val logoDrawable = resources.getDrawable(R.drawable.verticallogo)

            // Convert the drawable to a bitmap
            val logoBitmap = (logoDrawable as BitmapDrawable).bitmap

            // Define the desired width and height of the logo
            val desiredWidth = 100
            val desiredHeight = 100

            // Create a scaled bitmap of the logo
            val scaledLogoBitmap =
                Bitmap.createScaledBitmap(logoBitmap, desiredWidth, desiredHeight, true)

            // Resize the logo drawable to the calculated width and height
            canvas.drawText("Service Provider Report", 0f, 100f, paint)


            // Draw the logo on the canvas


            // Create a bitmap from the RecyclerView
            val recyclerViewBitmap = Bitmap.createBitmap(
                recyclerView.width,
                recyclerView.measuredHeight,
                Bitmap.Config.ARGB_8888
            )
            val recyclerViewCanvas = Canvas(recyclerViewBitmap)
            recyclerView.draw(recyclerViewCanvas)
            canvas.drawBitmap(recyclerViewBitmap, 0f, (80 + desiredHeight).toFloat(), null)

            // Finish the page and save the document to the file
            document.finishPage(page)
            val outputStream = FileOutputStream(pdfFileDirectory)
            document.writeTo(outputStream)
            document.close()
            // Display a success message
            Toast.makeText(context, "PDF created successfully", Toast.LENGTH_LONG).show()

            // Open the PDF in a PDF viewer app
            val contentUri = FileProvider.getUriForFile(
                context!!,
                context!!.applicationContext.packageName + ".fileprovider",
                pdfFileDirectory
            )
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(contentUri, "application/pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } catch (e: IOException) {
            e.printStackTrace()
            // Display an error message
            Toast.makeText(context, "Error creating PDF", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        // private List<ServiceHistoryItems> filteredItems;
        //E/SQLiteLog: (1) no such column: A.ServiceDetailID in "SELECT C.name, C.mobile, C.email, A.PickUpDateTime, A.PickUpReadyDate, A.DropOffTimeDate, A.AppointmentStatus, SD.ServiceName FROM APPOINTMENT A INNER JOIN CUSTOMER C ON A
        //D/AndroidRuntime: Shutting down VM
        //E/AndroidRuntime: FATAL EXCEPTION: main
        private const val QUERY_COMPLETED_APPOINTMENTS =
            "SELECT A.AppointmentID, C.name, C.mobile, C.email, A.AppointmentStatus, A.PickUpDateTime, A.PickUpReadyDate,A.DropOffTimeDate, A.ServiceProviderID,  A.AppointmentType, SD.ServiceName " +
                "FROM APPOINTMENT A " +
                "INNER JOIN CUSTOMER C ON A.Userid = C.Userid " +
                "INNER JOIN APPOINTMENT_DETAIL AD ON A.AppointmentID = AD.AppointmentID " +
                "INNER JOIN SERVICE_LIST SL ON AD.ServiceListID = SL.ServiceListID " +
                "INNER JOIN SERVICE_DETAIL SD ON SL.ServiceDetailID = SD.ServiceDetailID " +
                "WHERE A.ServiceProviderID = ? AND AppointmentStatus = 'Completed'"
        private const val QUERY_CANCELLED_APPOINTMENTS =
            "SELECT A.AppointmentID, C.name, C.mobile, C.email, A.AppointmentStatus, A.PickUpDateTime, A.PickUpReadyDate,A.DropOffTimeDate, A.ServiceProviderID, A.AppointmentType,SD.ServiceName " +
                "FROM APPOINTMENT A " +
                "INNER JOIN CUSTOMER C ON A.Userid = C.Userid " +
                "INNER JOIN APPOINTMENT_DETAIL AD ON A.AppointmentID = AD.AppointmentID " +
                "INNER JOIN SERVICE_LIST SL ON AD.ServiceListID = SL.ServiceListID " +
                "INNER JOIN SERVICE_DETAIL SD ON SL.ServiceDetailID = SD.ServiceDetailID " +
                "WHERE A.ServiceProviderID = ? AND AppointmentStatus = 'Cancelled'"

        //private static final String FILE_NAME = "ServiceReport_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ".pdf";
        private const val FILE_NAME = "ServiceReport_"
    }
}
