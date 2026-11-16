package com.baothanhbin.shortcompose.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baothanhbin.shortcompose.core.database.model.FavoriteEntity
import com.baothanhbin.shortcompose.core.database.model.PlaybackProgressEntity
import com.baothanhbin.shortcompose.core.database.model.RecentlyWatchedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LibraryDao {
    @Query("SELECT * FROM favorites ORDER BY updatedAt DESC")
    fun observeFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM recently_watched ORDER BY watchedAt DESC")
    fun observeRecentlyWatched(): Flow<List<RecentlyWatchedEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE episodeId = :episodeId)")
    fun observeIsFavorite(episodeId: Long): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(entity: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE episodeId = :episodeId")
    suspend fun deleteFavorite(episodeId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun markWatched(entity: RecentlyWatchedEntity)

    @Query("SELECT * FROM playback_progress WHERE episodeId = :episodeId LIMIT 1")
    suspend fun getProgress(episodeId: Long): PlaybackProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(entity: PlaybackProgressEntity)
}
