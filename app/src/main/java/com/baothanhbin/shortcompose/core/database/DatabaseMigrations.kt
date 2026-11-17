package com.baothanhbin.shortcompose.core.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE favorites ADD COLUMN viewCount INTEGER NOT NULL DEFAULT 0"
        )
        database.execSQL(
            "ALTER TABLE recently_watched ADD COLUMN viewCount INTEGER NOT NULL DEFAULT 0"
        )
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS home_cache (
                id INTEGER NOT NULL,
                contentJson TEXT NOT NULL,
                languageCode TEXT NOT NULL,
                syncedAt INTEGER NOT NULL,
                PRIMARY KEY(id)
            )
            """.trimIndent()
        )
    }
}
