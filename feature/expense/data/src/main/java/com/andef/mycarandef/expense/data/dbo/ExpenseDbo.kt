package com.andef.mycarandef.expense.data.dbo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.andef.mycarandef.car.data.dbo.CarDbo
import com.andef.mycarandef.expense.domain.entities.ExpenseType

@Entity(
    tableName = "expense",
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
data class ExpenseDbo(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val amount: Double,
    val note: String?,
    val type: ExpenseType,
    val date: Int,
    @ColumnInfo(name = "car_id")
    val carId: Long
)