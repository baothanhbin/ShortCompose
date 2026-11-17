package com.baothanhbin.shortcompose.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Long,
    val title: String,
    val description: String = "",
    val posterUrl: String = "",
    val rating: Double = 0.0,
    val viewCount: Long = 0,
    val firstEpisodeId: Long? = null,
    val firstEpisodeUrl: String = "",
    val isFavorite: Boolean = false,
    val resumeEpisodeId: Long? = null,
    val currentEpisodeNumber: Int? = null,
    val totalEpisodeCount: Int = 0,
)

@Serializable
data class MovieCategory(val name: String, val queryName: String = name)

@Serializable
data class HomeContent(
    val banners: List<Movie>,
    val categories: List<MovieCategory>,
    val trending: List<Movie>,
    val newRelease: List<Movie>,
    val spotlight: List<Movie>,
)

data class Episode(
    val id: Long,
    val movieId: Long,
    val number: Int,
    val title: String,
    val videoUrl: String,
    val subtitles: List<SubtitleTrack> = emptyList(),
    val isLocked: Boolean = false,
)

data class SubtitleTrack(val languageCode: String, val languageName: String, val url: String)
