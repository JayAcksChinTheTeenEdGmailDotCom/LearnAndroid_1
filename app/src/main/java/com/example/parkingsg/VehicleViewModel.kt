package com.example.parkingsg

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class VehicleViewModel(application: Application): AndroidViewModel(application) {
    private var parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)
    private val repository: VehicleRepository

    val allVehicles: LiveData<List<Vehicle>>

    init {
        val vehiclesDao = VehicleRoomDatabase.getDatabase(application, scope).vehicleDao()
        repository = VehicleRepository(vehiclesDao)

        allVehicles = repository.allVehicles
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun insert(vehicle: Vehicle) = scope.launch(Dispatchers.IO) {
        repository.insert(vehicle)
    }

    fun delete(vehicle: Vehicle) = scope.launch(Dispatchers.IO) {
        repository.deleteVehicle(vehicle)
    }
}