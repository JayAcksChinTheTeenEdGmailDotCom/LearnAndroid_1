package com.example.parkingsg

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText

class AddVehicleActivity : AppCompatActivity() {
    private lateinit var vehiclePlateView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle)
        vehiclePlateView = findViewById<EditText>(R.id.vehiclePlate)

        val button = findViewById<Button>(R.id.button_add_vehicle)
        button.setOnClickListener {
            val replyIntent = Intent()
            if(TextUtils.isEmpty(vehiclePlateView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val word = vehiclePlateView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, word)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }
}
