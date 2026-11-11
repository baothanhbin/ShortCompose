package com.baothanhbin.shortcompose.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.baothanhbin.shortcompose.core.database.dao.SampleDao
import com.baothanhbin.shortcompose.core.database.model.SampleEntity

@Database(
    entities = [SampleEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sampleDao(): SampleDao
}
