package com.example.parkingsg

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_select_vehicle.*

class SelectVehicleActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var myDataset = arrayOf("ABC123C", "DEF456J", "GUIJUGUJH345")

    private lateinit var vehicleViewModel: VehicleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_vehicle)

        viewManager = LinearLayoutManager(this)
        selectVehicleRecycleView.layoutManager = viewManager

        viewAdapter = RecyclerAdapter(this)
        selectVehicleRecycleView.adapter = viewAdapter

        vehicleViewModel = ViewModelProviders.of(this).get(VehicleViewModel::class.java)

        vehicleViewModel.allVehicles.observe(this, Observer { vehicles -> vehicles?.let { viewAdapter.setVehicles(it)}})

        val addBtn = findViewById<Button>(R.id.button_add_new_vehicle)
        addBtn.setOnClickListener {
            val intent = Intent(this, AddVehicleActivity::class.java)
            startActivityForResult(intent, newVehicleActivityRequestCode)
        }

        //This is delBtn
        viewAdapter.onItemClick = {
//            Log.d("imgBtn", it.vehiclePlate)
            vehicleViewModel.delete(it)
        }
        //This is selectVehicle
        viewAdapter.onVehicleClick = {
            //I want to return the vehicle plate value to main activity's (R.id.car).text
            val replyIntent = Intent()
            val word = it.vehiclePlate
            //Log.d("selectVehicleActivity", word)
            replyIntent.putExtra(SelectVehicleActivity.EXTRA_REPLY, word)
            setResult(Activity.RESULT_OK, replyIntent)
            finish()
        }
    }

    companion object {
        const val newVehicleActivityRequestCode = 1
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("SVA","received activity result")

        if( requestCode == newVehicleActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.let {
                val vehicle = Vehicle(it.getStringExtra(AddVehicleActivity.EXTRA_REPLY))
                Log.d("SelectVehicleActivity", it.getStringExtra(AddVehicleActivity.EXTRA_REPLY))
                vehicleViewModel.insert(vehicle)
            }
        } else {
            Toast.makeText(applicationContext, R.string.empty_not_saved, Toast.LENGTH_LONG).show()
        }
    }

    fun addVehicle(view: View) {
        val addVehicleIntent = Intent(this, AddVehicleActivity::class.java)
        startActivity(addVehicleIntent)
    }


}
