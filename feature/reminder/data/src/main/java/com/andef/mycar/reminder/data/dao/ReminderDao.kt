package com.andef.mycar.reminder.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.andef.mycar.reminder.data.dbo.ReminderDbo
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Insert(onConflict = REPLACE)
    suspend fun addReminder(reminderDbo: ReminderDbo): Long

    @Query(
        """
        UPDATE reminder
        SET text = :text, date = :date, time = :time
        WHERE id = :id
        """
    )
    suspend fun changeReminder(id: Long, text: String, date: Int, time: Int)

    @Query("DELETE FROM reminder WHERE id = :id")
    suspend fun removeReminder(id: Long)

    @Query("SELECT * FROM reminder WHERE id = :id")
    suspend fun getReminder(id: Long): ReminderDbo

    @Query("SELECT * FROM reminder WHERE car_id = :carId")
    fun getRemindersByCarId(carId: Long): Flow<List<ReminderDbo>>
}