package com.baothanhbin.shortcompose.core.network

import kotlinx.serialization.Serializable

@Serializable
data class FilmDetailResponse(
    val id: Long? = null, val legacyId: String? = null, val subtitle: Boolean? = null,
    val rating: Double? = null, val viewCount: Long? = null, val searchCount: Long? = null,
    val totalEpisode: Int? = null, val names: List<LocaleTextResponse>? = null,
    val covers: List<LocaleTextResponse>? = null, val categories: List<GenreResponse>? = null,
    val episodes: List<SegmentResponse>? = null,
)

@Serializable
data class FilmListResponse(
    val id: Long, val legacyId: String? = null, val subtitle: Boolean = false,
    val rating: Double? = null, val viewCount: Long = 0, val searchCount: Long = 0,
    val totalEpisode: Int = 0, val firstEpisode: SegmentResponse? = null,
    val names: List<LocaleTextResponse> = emptyList(), val covers: List<LocaleTextResponse> = emptyList(),
    val categories: List<GenreResponse> = emptyList(),
)

@Serializable
data class FilmPageResponse(
    val items: List<FilmListResponse> = emptyList(), val page: Int = 0, val size: Int = 0,
    val totalElements: Long = 0, val totalPages: Int = 0, val first: Boolean = false,
    val last: Boolean = false,
)

@Serializable
data class GenreResponse(
    val id: Long? = null, val slug: String? = null,
    val translations: List<LocaleTextResponse>? = null,
)

@Serializable
data class LocaleTextResponse(
    val languageCode: String? = null, val languageName: String? = null, val value: String? = null,
)

@Serializable
data class SegmentResponse(
    val id: Long? = null, val legacyId: String? = null, val episodeNumber: Int? = null,
    val url: String? = null, val premium: Boolean? = null, val rating: Double? = null,
    val viewCount: Long? = null, val thumbnails: List<LocaleTextResponse>? = null,
    val subtitles: List<LocaleTextResponse>? = null,
)
