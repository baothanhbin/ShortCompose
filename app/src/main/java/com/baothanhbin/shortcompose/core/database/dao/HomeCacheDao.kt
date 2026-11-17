package com.baothanhbin.shortcompose.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baothanhbin.shortcompose.core.database.model.HomeCacheEntity

@Dao
interface HomeCacheDao {
    @Query("SELECT * FROM home_cache WHERE id = 1 LIMIT 1")
    suspend fun getHomeCache(): HomeCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveHomeCache(cache: HomeCacheEntity)
}
