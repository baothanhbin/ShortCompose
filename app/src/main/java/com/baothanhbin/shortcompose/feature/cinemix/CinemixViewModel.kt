package com.baothanhbin.shortcompose.feature.cinemix

import androidx.lifecycle.ViewModel
import androidx.annotation.StringRes
import com.baothanhbin.shortcompose.R
import androidx.lifecycle.viewModelScope
import com.baothanhbin.shortcompose.core.data.repository.LibraryRepository
import com.baothanhbin.shortcompose.core.data.repository.MovieRepository
import com.baothanhbin.shortcompose.core.model.HomeContent
import com.baothanhbin.shortcompose.core.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CinemixUiState(
    val isLoading: Boolean = true,
    val content: HomeContent? = null,
    val error: String? = null,
    @StringRes val errorRes: Int? = null,
)

@HiltViewModel
class CinemixViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val library: LibraryRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CinemixUiState())
    val uiState: StateFlow<CinemixUiState> = _uiState.asStateFlow()
    private var favoriteMovieIds = emptySet<Long>()

    init {
        observeFavorites()
        load()
    }

    fun load() = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        runCatching { repository.getHomeContent() }
            .onSuccess {
                _uiState.value = CinemixUiState(
                    content = it.withFavoriteMovieIds(favoriteMovieIds),
                    isLoading = false,
                )
            }
            .onFailure { _uiState.value = CinemixUiState(isLoading = false, error = it.message, errorRes = R.string.movie_error_load) }
    }

    fun toggleFavorite(movie: Movie) = viewModelScope.launch {
        val episodeId = movie.firstEpisodeId
            ?: repository.getEpisodes(movie.id).firstOrNull { !it.isLocked }?.id
            ?: return@launch
        library.toggleFavorite(movie, episodeId, movie.isFavorite)
    }

    private fun observeFavorites() = viewModelScope.launch {
        library.favorites.collect { favorites ->
            favoriteMovieIds = favorites.mapTo(mutableSetOf()) { it.movieId }
            _uiState.value = _uiState.value.copy(
                content = _uiState.value.content?.withFavoriteMovieIds(favoriteMovieIds)
            )
        }
    }
}

private fun HomeContent.withFavoriteMovieIds(favoriteMovieIds: Set<Long>) = copy(
    banners = banners.withFavoriteMovieIds(favoriteMovieIds),
    trending = trending.withFavoriteMovieIds(favoriteMovieIds),
    newRelease = newRelease.withFavoriteMovieIds(favoriteMovieIds),
    spotlight = spotlight.withFavoriteMovieIds(favoriteMovieIds),
)

private fun List<Movie>.withFavoriteMovieIds(favoriteMovieIds: Set<Long>) = map { movie ->
    movie.copy(isFavorite = movie.id in favoriteMovieIds)
}
