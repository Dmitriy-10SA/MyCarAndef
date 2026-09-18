package com.andef.mycarandef.data

import androidx.room.migration.Migration

val MIGRATION_1_2 = Migration(1, 2) { database ->
    database.execSQL(
        """
        CREATE TABLE expense_new (
            id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            amount INTEGER NOT NULL,
            note TEXT,
            type TEXT NOT NULL,
            date INTEGER NOT NULL,
            car_id INTEGER NOT NULL,
            FOREIGN KEY(car_id) REFERENCES car(id) ON UPDATE CASCADE ON DELETE CASCADE
        )
        """.trimIndent()
    )
    database.execSQL(
        """
        INSERT INTO expense_new (id, amount, note, type, date, car_id)
        SELECT id, CAST(ROUND(amount * 100) AS INTEGER), note, type, date, car_id
        FROM expense
        """.trimIndent()
    )
    database.execSQL("DROP TABLE expense")
    database.execSQL("ALTER TABLE expense_new RENAME TO expense")
}
