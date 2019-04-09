package com.example.parkingsg

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "vehicle_table")
data class Vehicle(@PrimaryKey @ColumnInfo(name = "vehicle_plate") val vehiclePlate: String) {

}