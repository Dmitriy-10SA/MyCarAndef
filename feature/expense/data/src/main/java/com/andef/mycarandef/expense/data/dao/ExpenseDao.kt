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
    @Query("SELECT * FROM expense")
    suspend fun getAllExpensesAsList(): List<ExpenseDbo>

    @Query("SELECT * FROM expense WHERE id = :id")
    suspend fun getExpenseById(id: Long): ExpenseDbo

    @Query("SELECT * FROM expense WHERE car_id = :carId AND date BETWEEN :startDate AND :endDate")
    fun getExpensesByCarId(carId: Long, startDate: Int, endDate: Int): Flow<List<ExpenseDbo>>

    @Query("SELECT * FROM expense WHERE car_id = :carId")
    suspend fun getExpensesByCarIdAsList(carId: Long): List<ExpenseDbo>

    @Insert(onConflict = REPLACE)
    suspend fun addExpense(expenseDbo: ExpenseDbo)

    @Query(
        """
        UPDATE expense
        SET amount = :amount, note = :note, type = :type, date = :date
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