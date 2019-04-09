package com.example.parkingsg

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface VehicleDao {

    @Insert
    fun insert(vehiclePlate: Vehicle)

    @Query("DELETE FROM vehicle_table")
    fun deleteAll()

    @Query("SELECT * FROM vehicle_table ORDER BY vehicle_plate ASC")
    fun getAllVehicle(): LiveData<List<Vehicle>>

    @Delete
    fun deleteVehicle(vehicle: Vehicle)
}