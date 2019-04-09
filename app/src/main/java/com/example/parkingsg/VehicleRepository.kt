package com.example.parkingsg

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread

class VehicleRepository(private val vehicleDao: VehicleDao) {
    val allVehicles : LiveData<List<Vehicle>> = vehicleDao.getAllVehicle()

    @WorkerThread
    suspend fun insert(vehicle:Vehicle) {
        vehicleDao.insert(vehicle)
    }

    @WorkerThread
    suspend fun deleteVehicle(vehicle: Vehicle) {
        vehicleDao.deleteVehicle(vehicle)
    }
}