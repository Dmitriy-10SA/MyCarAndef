package com.andef.mycar.backup.presentation

import com.andef.mycarandef.expense.domain.entities.Expense
import com.andef.mycarandef.expense.domain.entities.ExpenseType
import com.google.gson.JsonParser
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class GsonForBackupTest {
    @Test
    fun oldBackupAmountInRubles_isRestoredAsKopecks() {
        val json =
            """{"id":1,"amount":123.45,"note":"fuel","type":"FUEL","date":"2026-09-18","carId":7}"""

        val expense = gson.fromJson(json, Expense::class.java)

        assertEquals(12345L, expense.amount)
    }

    @Test
    fun newBackup_keepsTheExistingRublesJsonFormat() {
        val expense = gson.fromJson(
            """{"id":1,"amount":123.45,"note":null,"type":"OTHER","date":"2026-09-18","carId":7}""",
            Expense::class.java
        )

        val amount = JsonParser.parseString(gson.toJson(expense))
            .asJsonObject
            .get("amount")
            .asBigDecimal

        assertEquals(0, BigDecimal("123.45").compareTo(amount))
    }

    @Test
    fun backupRoundTrip_supportsEveryExpenseType() {
        ExpenseType.entries.forEach { type ->
            val expense = gson.fromJson(
                """{"id":1,"amount":123.45,"note":null,"type":"${type.name}","date":"2026-09-18","carId":7}""",
                Expense::class.java
            )

            val restored = gson.fromJson(gson.toJson(expense), Expense::class.java)

            assertEquals(type, restored.type)
        }
    }
}
