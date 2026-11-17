package com.baothanhbin.shortcompose.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.baothanhbin.shortcompose.core.database.dao.SampleDao
import com.baothanhbin.shortcompose.core.database.model.SampleEntity
import com.baothanhbin.shortcompose.core.database.dao.LibraryDao
import com.baothanhbin.shortcompose.core.database.dao.HomeCacheDao
import com.baothanhbin.shortcompose.core.database.model.FavoriteEntity
import com.baothanhbin.shortcompose.core.database.model.HomeCacheEntity
import com.baothanhbin.shortcompose.core.database.model.RecentlyWatchedEntity
import com.baothanhbin.shortcompose.core.database.model.PlaybackProgressEntity

@Database(
    entities = [SampleEntity::class, FavoriteEntity::class, RecentlyWatchedEntity::class, PlaybackProgressEntity::class, HomeCacheEntity::class],
    version = 4,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sampleDao(): SampleDao
    abstract fun libraryDao(): LibraryDao
    abstract fun homeCacheDao(): HomeCacheDao
}
