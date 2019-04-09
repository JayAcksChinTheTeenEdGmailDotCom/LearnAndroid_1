package com.example.parkingsg

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.parkingsg.SelectVehicleActivity.Companion.newVehicleActivityRequestCode
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        styleDurationDisplay(getString(R.string.sessionDuration))
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        //Copied from MapActivity
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val selectBtn = findViewById<Button>(R.id.car)
        selectBtn.setOnClickListener {
            val intent = Intent(this, SelectVehicleActivity::class.java)
            startActivityForResult(intent, selectVehicleActivityRequestCode)
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        // Set my current location
        mMap.setMyLocationEnabled(true)


    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }

            R.id.nav_feedback -> {

            }
//            R.id.nav_share -> {
//
//            }
//            R.id.nav_send -> {
//
//            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("SVA","received activity result")

        if( requestCode == MainActivity.selectVehicleActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.let {
                val vehicle = Vehicle(it.getStringExtra(SelectVehicleActivity.EXTRA_REPLY))
                Log.d("SelectVehicleActivity", it.getStringExtra(SelectVehicleActivity.EXTRA_REPLY))
                //vehicleViewModel.insert(vehicle)
                //I will display the text in R.id.car 's text
                var carPlate = findViewById<Button>(R.id.car)
                carPlate.text = it.getStringExtra(SelectVehicleActivity.EXTRA_REPLY)
            }
        } else {
            Toast.makeText(applicationContext, R.string.empty_not_saved, Toast.LENGTH_LONG).show()
        }
    }

    fun increaseDuration(view: View) {
        //Add 30 mins every time this function is called.
        val increaseBy = 30

        //Get Text View
        val timerDisplay = findViewById<TextView>(R.id.displayDuration)
        //Get value of text as String
        val timerDisplayString = timerDisplay.text.toString()
        //Convert string to integer
        var timerDisplayInt = convertDuration2Minute(timerDisplayString)
        //Add 30 mins
        timerDisplayInt += increaseBy

        //Updates text view with new value
        val displayText = convertMinute2Duration(timerDisplayInt)
        styleDurationDisplay(displayText)
    }

    fun decreaseDuration(view: View) {
        //Minus 30 mins every time this function is called but never go below zero.
        val decreaseVal = 30

        //Get Text View
        val timerDisplay = findViewById<TextView>(R.id.displayDuration)
        //Get value of text as String
        val timerDisplayString = timerDisplay.text.toString()
        //Convert string to integer
//        var timerDisplayInt = Integer.parseInt(timerDisplayString)
        var timerDisplayInt = convertDuration2Minute(timerDisplayString)

        //Check if timer is not equal to zero.
        if(timerDisplayInt != 0){
            //If true, minus 30 mins
            timerDisplayInt -= decreaseVal
        } else {
            //Else do nothing
        }
        //Updates text view with new value
        val displayText = convertMinute2Duration(timerDisplayInt)
        styleDurationDisplay(displayText)
    }

    private fun styleDurationDisplay(displayDurationText: String) {
        val ssDurationDisplay = SpannableString(displayDurationText)
        val hour = "HR"
        val minute = "MIN"

        //If HR exists, make HR smaller
        if(displayDurationText.contains(hour, true)){
            ssDurationDisplay.setSpan(RelativeSizeSpan(0.5f), ssDurationDisplay.indexOf(hour), ssDurationDisplay.indexOf(hour) + hour.count(), 0)
        }
        //If MIN exists, make MIN smaller. Since MIN always exists, I do not need an IF here.
//        if(displayDurationText.contains(minute, true)){
        ssDurationDisplay.setSpan(RelativeSizeSpan(0.5f), ssDurationDisplay.indexOf(minute), ssDurationDisplay.indexOf(minute) + minute.count(), 0)
//        }

        //Set spanned text to text view, displayDuration
        val displayDuration = findViewById<TextView>(R.id.displayDuration)
        displayDuration.text = ssDurationDisplay
    }

    private fun convertDuration2Minute(displayDurationText: String): Int {
        //Get value of HR. At this point, hour is String. Will convert later. Important, always account for when there is no HR!!!
        var hour = "0"
        if(displayDurationText.contains("HR", true)) {
            hour = displayDurationText.substring(0, displayDurationText.indexOf("HR"))
        }
        //Get value of MIN
        val minute = displayDurationText.substring(displayDurationText.indexOf("MIN") - 2,displayDurationText.indexOf("MIN"))
        //Calculate minute

        val hr2min = Integer.parseInt(hour) * 60

        return hr2min + Integer.parseInt(minute)
    }

    private fun convertMinute2Duration(duration: Int): String {
        //var returnStr: String
        //Convert Integer to Long to use TimeUnit lib
        val timerDisplayLong = duration.toLong()
        val timerDisplayHour = TimeUnit.MINUTES.toHours(timerDisplayLong)
        val timerDisplayMinute = timerDisplayLong - TimeUnit.HOURS.toMinutes(timerDisplayHour)

        //Return duration display in string
        return when(timerDisplayHour < 1) {
            true -> String.format("%02dMIN", timerDisplayMinute)
            false -> String.format("%1dHR %02dMIN", timerDisplayHour, timerDisplayMinute)
        }
    }

    fun selectVehicle(view: View){
        val selectVehicleIntent = Intent(this, SelectVehicleActivity::class.java)

        startActivity(selectVehicleIntent)
    }

    fun launchMap(view: View){
        val mapIntent = Intent(this, MapsActivity::class.java)
        startActivity(mapIntent)
    }

    companion object {
        const val selectVehicleActivityRequestCode = 1
    }
}
