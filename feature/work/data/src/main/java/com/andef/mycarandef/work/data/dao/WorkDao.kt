package com.andef.mycarandef.work.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.andef.mycarandef.work.data.dbo.WorkDbo
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkDao {
    @Insert(onConflict = REPLACE)
    suspend fun addWork(workDbo: WorkDbo)

    @Query(
        """
        UPDATE work
        SET title = :title AND note = :note AND mileage = :mileage AND date = :date
        WHERE id = :id
        """
    )
    suspend fun changeWork(
        id: Long,
        title: String,
        note: String?,
        mileage: Int,
        date: Int
    )

    @Query("DELETE FROM work WHERE id = :id")
    suspend fun removeWork(id: Long)

    @Query("SELECT * FROM work WHERE id = :id")
    suspend fun getWorkById(id: Long): WorkDbo

    @Query("SELECT * FROM work WHERE car_id = :carId")
    fun getWorksByCarId(carId: Long): Flow<List<WorkDbo>>
}