package com.baothanhbin.shortcompose.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baothanhbin.shortcompose.core.database.model.SampleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SampleDao {
    @Query("SELECT * FROM sample_items ORDER BY createdAt DESC")
    fun observeItems(): Flow<List<SampleEntity>>

    @Query("SELECT COUNT(*) FROM sample_items")
    suspend fun getItemCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: SampleEntity)
}
