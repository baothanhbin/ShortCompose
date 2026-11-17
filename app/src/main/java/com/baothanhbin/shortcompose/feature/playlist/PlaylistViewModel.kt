package com.baothanhbin.shortcompose.feature.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baothanhbin.shortcompose.core.data.repository.LibraryRepository
import com.baothanhbin.shortcompose.core.data.repository.MovieRepository
import com.baothanhbin.shortcompose.core.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

enum class PlaylistTab { RECENTLY_WATCHED, FAVORITES }
data class PlaylistUiState(
    val selectedTab: PlaylistTab = PlaylistTab.RECENTLY_WATCHED,
    val movies: List<Movie> = emptyList()
)

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    repository: LibraryRepository,
    movieRepository: MovieRepository,
) : ViewModel() {
    private val tab = MutableStateFlow(PlaylistTab.RECENTLY_WATCHED)
    val uiState = combine(
        tab, repository.recentlyWatched, repository.favorites
    ) { selected, recent, favorites ->
        val storedCounts = (recent.map { it.movieId to it.viewCount } +
            favorites.map { it.movieId to it.viewCount })
            .groupBy(keySelector = { it.first }, valueTransform = { it.second })
            .mapValues { (_, counts) -> counts.maxOrNull() ?: 0L }
        val viewCounts = storedCounts.keys.associateWith { movieId ->
            storedCounts[movieId]?.takeIf { it > 0 }
                ?: runCatching { movieRepository.getMovie(movieId)?.viewCount }
                    .getOrNull()
                ?: 0L
        }
        val movies = when (selected) {
            PlaylistTab.RECENTLY_WATCHED -> recent.map { watched ->
                Movie(
                    id = watched.movieId,
                    title = watched.title,
                    posterUrl = watched.posterUrl,
                    resumeEpisodeId = watched.episodeId,
                    currentEpisodeNumber = watched.episodeNumber,
                    totalEpisodeCount = watched.totalEpisodeCount,
                    viewCount = viewCounts[watched.movieId] ?: 0,
                    isFavorite = favorites.any { it.episodeId == watched.episodeId },
                )
            }

            PlaylistTab.FAVORITES -> favorites.map { favorite ->
                Movie(
                    id = favorite.movieId, title = favorite.title, posterUrl = favorite.posterUrl,
                    viewCount = viewCounts[favorite.movieId] ?: 0,
                    resumeEpisodeId = favorite.episodeId, isFavorite = true,
                )
            }
        }
        PlaylistUiState(selected, movies)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PlaylistUiState())

    fun selectTab(value: PlaylistTab) {
        tab.value = value
    }
}
