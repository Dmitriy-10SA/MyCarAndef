package com.andef.mycar.reminder.domain.entities

enum class ReminderRepeatType(val title: String, val titleForUser: String) {
    WEEKLY(title = "WEEKLY", titleForUser = "Каждую неделю"),
    MONTHLY(title = "MONTHLY", titleForUser = "Каждый месяц"),
    EVERY_THREE_MONTHS(
        title = "EVERY_THREE_MONTHS",
        titleForUser = "Каждые 3 месяца"
    ),
    YEARLY(title = "YEARLY", titleForUser = "Каждый год"),
    FIRST_DAY_OF_MONTH(
        title = "FIRST_DAY_OF_MONTH",
        titleForUser = "В первый день месяца"
    ),
    LAST_DAY_OF_MONTH(
        title = "LAST_DAY_OF_MONTH",
        titleForUser = "В последний день месяца"
    )
}
