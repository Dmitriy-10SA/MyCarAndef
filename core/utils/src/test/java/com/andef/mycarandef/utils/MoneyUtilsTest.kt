package com.andef.mycarandef.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MoneyUtilsTest {
    @Test
    fun normalizeAmountInput_acceptsCommaAndLimitsFractionToTwoDigits() {
        assertEquals("1234.56", normalizeAmountInput(" 001 234,567 "))
    }

    @Test
    fun normalizeAmountInput_keepsIncompleteDecimalInputEditable() {
        assertEquals("0.", normalizeAmountInput(","))
        assertEquals("12.", normalizeAmountInput("12."))
    }

    @Test
    fun parseAmountToKopecks_usesIntegerMinorUnits() {
        assertEquals(12345L, parseAmountToKopecks("123,45"))
        assertEquals(12340L, parseAmountToKopecks("123.4"))
        assertEquals(12300L, parseAmountToKopecks("123"))
    }

    @Test
    fun parseAmountToKopecks_rejectsOverflow() {
        assertNull(parseAmountToKopecks(Long.MAX_VALUE.toString()))
    }

    @Test
    fun moneyFormatting_readsKopecksExactly() {
        assertEquals("123,45", formatAmountForEdit(12345L))
        assertEquals("1 234.05₽", formatPriceRuble(123405L))
    }
}
