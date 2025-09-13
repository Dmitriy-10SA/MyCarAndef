package com.andef.mycarandef.work.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.andef.mycarandef.work.data.dbo.WorkDbo
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkDao {
    @Query("SELECT * FROM work")
    suspend fun getAllWorksAsList(): List<WorkDbo>

    @Insert(onConflict = REPLACE)
    suspend fun addWork(workDbo: WorkDbo)

    @Query(
        """
        UPDATE work
        SET title = :title, note = :note, mileage = :mileage, date = :date
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

    @Query("SELECT * FROM work WHERE car_id = :carId ORDER BY mileage DESC, date DESC")
    fun getWorksByCarId(carId: Long): Flow<List<WorkDbo>>
}