package com.andef.mycarandef.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [], exportSchema = false, version = 1)
abstract class MyCarDatabase : RoomDatabase() {
    companion object {
        private const val DB_NAME = "my-car-db"

        private var instance: MyCarDatabase? = null
        fun getInstance(application: Application): MyCarDatabase {
            if (instance != null) return instance!!
            synchronized(this) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        application,
                        MyCarDatabase::class.java,
                        DB_NAME
                    ).build()
                }
                return instance!!
            }
        }
    }
}