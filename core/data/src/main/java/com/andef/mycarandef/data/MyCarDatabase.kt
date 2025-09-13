package com.andef.mycarandef.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.andef.mycar.reminder.data.dao.ReminderDao
import com.andef.mycar.reminder.data.dbo.ReminderDbo
import com.andef.mycarandef.car.data.dao.CarDao
import com.andef.mycarandef.car.data.dbo.CarDbo
import com.andef.mycarandef.expense.data.dao.ExpenseDao
import com.andef.mycarandef.expense.data.dbo.ExpenseDbo
import com.andef.mycarandef.work.data.dao.WorkDao
import com.andef.mycarandef.work.data.dbo.WorkDbo

@Database(
    entities = [
        CarDbo::class,
        ExpenseDbo::class,
        WorkDbo::class,
        ReminderDbo::class
    ],
    exportSchema = false,
    version = 1
)
abstract class MyCarDatabase : RoomDatabase() {
    abstract val carDao: CarDao
    abstract val expenseDao: ExpenseDao
    abstract val workDao: WorkDao
    abstract val reminderDao: ReminderDao

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