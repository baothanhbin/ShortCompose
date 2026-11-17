package com.baothanhbin.shortcompose.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "home_cache")
data class HomeCacheEntity(
    @PrimaryKey val id: Int = HOME_CACHE_ID,
    val contentJson: String,
    val languageCode: String,
    val syncedAt: Long,
) {
    companion object {
        const val HOME_CACHE_ID = 1
    }
}
