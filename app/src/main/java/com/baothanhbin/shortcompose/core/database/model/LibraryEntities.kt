package com.baothanhbin.shortcompose.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val episodeId: Long,
    val movieId: Long,
    val title: String,
    val posterUrl: String,
    val viewCount: Long = 0,
    val updatedAt: Long = System.currentTimeMillis(),
)

@Entity(tableName = "recently_watched", primaryKeys = ["movieId", "episodeId"])
data class RecentlyWatchedEntity(
    val movieId: Long,
    val episodeId: Long,
    val title: String,
    val posterUrl: String,
    val episodeNumber: Int,
    val totalEpisodeCount: Int,
    val viewCount: Long = 0,
    val watchedAt: Long = System.currentTimeMillis(),
)

@Entity(tableName = "playback_progress")
data class PlaybackProgressEntity(
    @PrimaryKey val episodeId: Long,
    val movieId: Long,
    val positionMs: Long,
    val durationMs: Long,
    val updatedAt: Long = System.currentTimeMillis(),
)
