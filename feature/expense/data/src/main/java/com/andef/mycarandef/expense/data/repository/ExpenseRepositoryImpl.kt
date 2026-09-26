package com.andef.mycarandef.expense.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.andef.mycarandef.expense.data.dao.ExpenseDao
import com.andef.mycarandef.expense.data.mapper.ExpenseMapper
import com.andef.mycarandef.expense.domain.entities.Expense
import com.andef.mycarandef.expense.domain.entities.ExpenseType
import com.andef.mycarandef.expense.domain.repository.ExpenseRepository
import com.andef.mycarandef.utils.toInt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val expenseMapper: ExpenseMapper,
    private val expenseDao: ExpenseDao,
    private val sharedPreferences: SharedPreferences
) : ExpenseRepository {
    override fun getDefaultExpenseType(): ExpenseType? {
        val savedType = sharedPreferences.getString(DEFAULT_EXPENSE_TYPE, null)
        return ExpenseType.entries.find { it.name == savedType }
    }

    override fun setDefaultExpenseType(type: ExpenseType?) {
        sharedPreferences.edit {
            if (type == null) remove(DEFAULT_EXPENSE_TYPE)
            else putString(DEFAULT_EXPENSE_TYPE, type.name)
        }
    }

    override suspend fun getAllExpensesAsList(): List<Expense> {
        return expenseDao.getAllExpensesAsList().map { expenseDbo -> expenseMapper.map(expenseDbo) }
    }

    override suspend fun addExpense(expense: Expense) {
        expenseDao.addExpense(expenseMapper.map(expense))
    }

    override suspend fun changeExpense(
        id: Long,
        amount: Long,
        note: String?,
        type: ExpenseType,
        date: LocalDate
    ) {
        expenseDao.changeExpense(id, amount, note, type, date.toInt())
    }

    override suspend fun removeExpense(id: Long) {
        expenseDao.removeExpense(id)
    }

    override suspend fun getExpensesByCarIdAsList(carId: Long): List<Expense> {
        return expenseDao.getExpensesByCarIdAsList(carId).map { expenseDbo ->
            expenseMapper.map(expenseDbo)
        }
    }

    override suspend fun getExpenseById(id: Long): Expense {
        return expenseMapper.map(expenseDao.getExpenseById(id))
    }

    override fun getExpensesByCarId(
        carId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<Expense>> {
        return expenseDao.getExpensesByCarId(carId, startDate.toInt(), endDate.toInt())
            .map { expensesDbo ->
                expensesDbo.map { expenseDbo ->
                    expenseMapper.map(expenseDbo)
                }
            }
    }

    private companion object {
        const val DEFAULT_EXPENSE_TYPE = "default-expense-type"
    }
}
