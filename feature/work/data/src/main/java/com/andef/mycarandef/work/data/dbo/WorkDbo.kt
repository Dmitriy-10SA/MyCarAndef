package com.andef.mycarandef.work.data.dbo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.andef.mycarandef.car.data.dbo.CarDbo

@Entity(
    tableName = "work",
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
data class WorkDbo(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val note: String?,
    val mileage: Int,
    val date: Int,
    @ColumnInfo(name = "car_id")
    val carId: Long
)
