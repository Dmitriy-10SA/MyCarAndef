package com.andef.mycarandef.expense.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.andef.mycarandef.expense.data.dbo.ExpenseDbo
import com.andef.mycarandef.expense.domain.entities.ExpenseType
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expense WHERE id = :id")
    suspend fun getExpenseById(id: Long): ExpenseDbo

    @Query("SELECT * FROM expense WHERE car_id = :carId")
    fun getExpensesByCarId(carId: Int): Flow<List<ExpenseDbo>>

    @Insert(onConflict = REPLACE)
    suspend fun addExpense(expenseDbo: ExpenseDbo)

    @Query(
        """
        UPDATE expense
        SET amount = :amount AND note = :note AND type = :type AND date = :date
        WHERE id = :id
        """
    )
    suspend fun changeExpense(
        id: Long,
        amount: Double,
        note: String?,
        type: ExpenseType,
        date: Int
    )

    @Query("DELETE FROM expense WHERE id = :id")
    suspend fun removeExpense(id: Long)
}