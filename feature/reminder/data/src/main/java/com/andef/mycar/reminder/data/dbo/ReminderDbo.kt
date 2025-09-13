package com.andef.mycar.reminder.data.dbo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.andef.mycarandef.car.data.dbo.CarDbo

@Entity(
    tableName = "reminder",
    foreignKeys = [
        ForeignKey(
            entity = CarDbo::class,
            parentColumns = ["id"],
            childColumns = ["car_id"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ]
)
data class ReminderDbo(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val text: String,
    val date: Int,
    val time: Int,
    @ColumnInfo(name = "car_id")
    val carId: Long,
    @ColumnInfo(name = "car_name")
    val carName: String
)