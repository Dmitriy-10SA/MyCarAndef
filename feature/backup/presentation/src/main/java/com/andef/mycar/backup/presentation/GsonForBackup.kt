package com.andef.mycar.backup.presentation

import android.content.Context
import android.net.Uri
import com.andef.mycar.backup.domain.BackupData
import com.andef.mycarandef.expense.domain.entities.Expense
import com.andef.mycarandef.expense.domain.entities.ExpenseType
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private class LocalDateAdapter : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun serialize(
        src: LocalDate?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src?.format(formatter))
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDate {
        return LocalDate.parse(json?.asString, formatter)
    }
}

private class LocalTimeAdapter : JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_TIME
    override fun serialize(src: LocalTime?, typeOfSrc: Type?, context: JsonSerializationContext?) =
        JsonPrimitive(src?.format(formatter))

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ) =
        LocalTime.parse(json?.asString, formatter)
}

private class ExpenseAdapter : JsonSerializer<Expense>, JsonDeserializer<Expense> {
    override fun serialize(
        src: Expense,
        typeOfSrc: Type?,
        context: JsonSerializationContext
    ): JsonElement = JsonObject().apply {
        addProperty("id", src.id)
        add("amount", JsonPrimitive(BigDecimal.valueOf(src.amount, 2)))
        add("note", src.note?.let(::JsonPrimitive) ?: JsonNull.INSTANCE)
        addProperty("type", src.type.name)
        add("date", context.serialize(src.date, LocalDate::class.java))
        addProperty("carId", src.carId)
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext
    ): Expense {
        val value = json.asJsonObject
        val amountInKopecks = value.get("amount")
            .asBigDecimal
            .movePointRight(2)
            .setScale(0, RoundingMode.HALF_UP)
            .longValueExact()

        return Expense(
            id = value.get("id").asLong,
            amount = amountInKopecks,
            note = value.get("note")?.takeUnless { it.isJsonNull }?.asString,
            type = ExpenseType.valueOf(value.get("type").asString),
            date = context.deserialize(value.get("date"), LocalDate::class.java),
            carId = value.get("carId").asLong
        )
    }
}

fun importDataFromJson(uri: Uri, context: Context): BackupData? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val json = inputStream?.bufferedReader().use { it?.readText() }
        json?.let { gson.fromJson(it, BackupData::class.java) }
    } catch (_: Exception) {
        null
    }
}

val gson = GsonBuilder()
    .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
    .registerTypeAdapter(LocalTime::class.java, LocalTimeAdapter())
    .registerTypeAdapter(Expense::class.java, ExpenseAdapter())
    .create()
