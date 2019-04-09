package com.example.parkingsg

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Vehicle::class], version = 1)
public abstract class VehicleRoomDatabase : RoomDatabase() {

    abstract fun vehicleDao() : VehicleDao

    companion object {
        @Volatile
        private var INSTANCE: VehicleRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): VehicleRoomDatabase {
            return INSTANCE?: synchronized(this) {
                // Create database here
                val instance = Room.databaseBuilder(context.applicationContext, VehicleRoomDatabase::class.java, "Vehicle_database").addCallback(VehicleDatabaseCallback(scope)).build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class VehicleDatabaseCallback(private val scope: CoroutineScope): RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database -> scope.launch(Dispatchers.IO) {
                populateDatabase(database.vehicleDao())
            }}
        }

        fun populateDatabase(vehicleDao: VehicleDao) {
            vehicleDao.deleteAll()

            var plate = Vehicle("ABC123C")
            vehicleDao.insert(plate)
            plate = Vehicle("DEF456J")
            vehicleDao.insert(plate)
        }
    }
}