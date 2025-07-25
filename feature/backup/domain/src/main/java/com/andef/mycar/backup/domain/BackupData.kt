package com.andef.mycar.backup.domain

import com.andef.mycar.reminder.domain.entities.Reminder
import com.andef.mycarandef.car.domain.entities.Car
import com.andef.mycarandef.expense.domain.entities.Expense
import com.andef.mycarandef.work.domain.entities.Work

data class BackupData(
    val allCars: List<Car>,
    val allExpenses: List<Expense>,
    val allWorks: List<Work>,
    val allReminders: List<Reminder>,
    val username: String
)