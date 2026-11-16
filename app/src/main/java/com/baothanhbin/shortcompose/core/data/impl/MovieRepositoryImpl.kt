package com.baothanhbin.shortcompose.core.data.impl

import com.baothanhbin.shortcompose.core.data.repository.MovieRepository
import com.baothanhbin.shortcompose.core.model.Episode
import com.baothanhbin.shortcompose.core.model.HomeContent
import com.baothanhbin.shortcompose.core.model.Movie
import com.baothanhbin.shortcompose.core.model.MovieCategory
import com.baothanhbin.shortcompose.core.model.SubtitleTrack
import com.baothanhbin.shortcompose.core.network.FilmDetailResponse
import com.baothanhbin.shortcompose.core.network.FilmListResponse
import com.baothanhbin.shortcompose.core.network.GenreResponse
import com.baothanhbin.shortcompose.core.network.LocaleTextResponse
import com.baothanhbin.shortcompose.core.network.NetworkDataSource
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class MovieRepositoryImpl @Inject constructor(
    private val network: NetworkDataSource,
) : MovieRepository {
    private val details = ConcurrentHashMap<Long, FilmDetailResponse>()

    override suspend fun getHomeContent(language: String): HomeContent = coroutineScope {
        val trending = async { network.getTopViewedMovies() }
        val releases = async { network.getAllMoviesByPage(size = 20).items }
        val spotlight = async { network.getTopRatedMovies() }
        val categoryNames = async { network.getCategoryNames() }
        val trendingResponse = trending.await()
        val releasesResponse = releases.await()
        val spotlightResponse = spotlight.await()
        val genres = (trendingResponse + releasesResponse + spotlightResponse).flatMap { it.categories }
        val trendingMovies = trendingResponse.map { it.toMovie(language) }
        HomeContent(
            banners = trendingMovies.take(7),
            categories = categoryNames.await().map { raw ->
                val genre = genres.firstOrNull { it.matches(raw) }
                MovieCategory(genre?.translations.orEmpty().pick(language).ifBlank { raw }, raw)
            },
            trending = trendingMovies,
            newRelease = releasesResponse.map { it.toMovie(language) },
            spotlight = spotlightResponse.map { it.toMovie(language) },
        )
    }

    override suspend fun getMoviesByCategory(category: String, language: String): List<Movie> =
        network.getMoviesByCategory(category, size = 50).items.map { it.toMovie(language) }

    override suspend fun searchMovies(query: String, language: String): List<Movie> =
        (if (query.isBlank()) network.getTopSearchedMovies()
        else network.searchMovies(query, size = 50).items).map { it.toMovie(language) }

    override suspend fun getTrending(language: String): List<Movie> =
        network.getTopViewedMovies().map { it.toMovie(language) }

    override suspend fun getNewReleases(language: String): List<Movie> =
        network.getAllMoviesByPage(size = 50).items.map { it.toMovie(language) }

    override suspend fun getSpotlight(language: String): List<Movie> =
        network.getTopRatedMovies().map { it.toMovie(language) }

    override suspend fun getMovie(movieId: Long, language: String): Movie? =
        detail(movieId).toMovie(language)

    override suspend fun getEpisodes(movieId: Long, language: String): List<Episode> {
        val detail = detail(movieId)
        val available = detail.episodes.orEmpty().mapIndexed { index, segment ->
            val number = (segment.episodeNumber ?: index) + 1
            Episode(
                id = segment.id ?: -number.toLong(),
                movieId = movieId,
                number = number,
                title = "Episode $number",
                videoUrl = segment.url.orEmpty(),
                subtitles = segment.subtitles.orEmpty().mapIndexedNotNull { subtitleIndex, item ->
                    val url = item.value?.takeIf(String::isNotBlank) ?: return@mapIndexedNotNull null
                    val code = item.languageCode?.takeIf(String::isNotBlank) ?: "und-$subtitleIndex"
                    SubtitleTrack(code, item.languageName?.takeIf(String::isNotBlank) ?: code.uppercase(), url)
                },
                isLocked = segment.url.isNullOrBlank(),
            )
        }
        val byNumber = available.associateBy(Episode::number)
        val total = maxOf(detail.totalEpisode ?: 0, available.maxOfOrNull(Episode::number) ?: 0)
        return (1..total).map { number -> byNumber[number] ?: Episode(
            id = -number.toLong(), movieId = movieId, number = number,
            title = "Episode $number", videoUrl = "", isLocked = true,
        ) }
    }

    private suspend fun detail(movieId: Long): FilmDetailResponse =
        details[movieId] ?: network.getMovieDetail(movieId).also { details[movieId] = it }

    private fun FilmListResponse.toMovie(language: String) = Movie(
        id = id,
        title = names.pick(language),
        description = categories.mapNotNull(GenreResponse::slug).joinToString(" · "),
        posterUrl = covers.pick(language),
        rating = rating ?: 0.0,
        viewCount = viewCount,
        firstEpisodeId = firstEpisode?.id,
        firstEpisodeUrl = firstEpisode?.url.orEmpty(),
        totalEpisodeCount = totalEpisode,
    )

    private fun FilmDetailResponse.toMovie(language: String): Movie? = id?.let { movieId ->
        Movie(
            id = movieId,
            title = names.orEmpty().pick(language),
            description = categories.orEmpty().mapNotNull(GenreResponse::slug).joinToString(" · "),
            posterUrl = covers.orEmpty().pick(language),
            rating = rating ?: 0.0,
            viewCount = viewCount ?: 0,
            totalEpisodeCount = totalEpisode ?: 0,
        )
    }

    private fun List<LocaleTextResponse>.pick(language: String): String {
        val code = language.substringBefore('-')
        return firstOrNull { it.languageCode?.startsWith(code, true) == true }?.value
            ?: firstOrNull { it.languageCode?.startsWith("en", true) == true }?.value
            ?: firstNotNullOfOrNull(LocaleTextResponse::value).orEmpty()
    }

    private fun GenreResponse.matches(value: String) = slug.equals(value, true) ||
        translations.orEmpty().any { it.value.equals(value, true) }
}
