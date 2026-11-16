package com.baothanhbin.shortcompose.feature.player

import androidx.lifecycle.SavedStateHandle
import androidx.annotation.StringRes
import com.baothanhbin.shortcompose.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baothanhbin.shortcompose.core.data.repository.LibraryRepository
import com.baothanhbin.shortcompose.core.data.repository.MovieRepository
import com.baothanhbin.shortcompose.core.model.Episode
import com.baothanhbin.shortcompose.core.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PlayerUiState(
    val loading: Boolean = true,
    val movie: Movie? = null,
    val episodes: List<Episode> = emptyList(),
    val selectedIndex: Int = 0,
    val resumePositionMs: Long = 0,
    val isFavorite: Boolean = false,
    val error: String? = null,
    @StringRes val errorRes: Int? = null,
) { val episode: Episode? get() = episodes.getOrNull(selectedIndex) }

@HiltViewModel
class PlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val movies: MovieRepository,
    private val library: LibraryRepository,
) : ViewModel() {
    private val movieId: Long = checkNotNull(savedStateHandle["movieId"])
    private val _state = MutableStateFlow(PlayerUiState())
    val state = _state.asStateFlow()
    private var favoriteJob: Job? = null

    init { load() }

    fun load() = viewModelScope.launch {
        _state.value = PlayerUiState()
        runCatching {
            val movie = movies.getMovie(movieId) ?: error("movie_not_found")
            val episodes = movies.getEpisodes(movieId)
            movie to episodes
        }.onSuccess { (movie, episodes) ->
            val first = episodes.indexOfFirst { !it.isLocked }.coerceAtLeast(0)
            _state.value = PlayerUiState(false, movie, episodes, first)
            observeFavorite()
            loadProgress()
        }.onFailure { _state.value = PlayerUiState(false, error = it.message?.takeUnless { message -> message == "movie_not_found" }, errorRes = R.string.player_error_load) }
    }

    fun selectEpisode(index: Int) {
        if (_state.value.episodes.getOrNull(index)?.isLocked != false) return
        _state.value = _state.value.copy(selectedIndex = index, resumePositionMs = 0)
        observeFavorite()
        viewModelScope.launch { loadProgress() }
    }

    fun toggleFavorite() {
        val state = _state.value
        val movie = state.movie ?: return
        val episode = state.episode ?: return
        viewModelScope.launch { library.toggleFavorite(movie, episode.id, state.isFavorite) }
    }

    fun markWatched() {
        val state = _state.value
        val movie = state.movie ?: return
        val episode = state.episode ?: return
        viewModelScope.launch { library.markWatched(movie, episode.id, episode.number, state.episodes.size) }
    }

    fun saveProgress(positionMs: Long, durationMs: Long) {
        val episode = _state.value.episode ?: return
        viewModelScope.launch { library.saveProgress(episode.id, movieId, positionMs, durationMs) }
    }

    private fun observeFavorite() {
        favoriteJob?.cancel()
        val episode = _state.value.episode ?: return
        favoriteJob = viewModelScope.launch {
            library.isFavorite(episode.id).collect { favorite -> _state.value = _state.value.copy(isFavorite = favorite) }
        }
    }

    private suspend fun loadProgress() {
        val episode = _state.value.episode ?: return
        val progress = library.getProgress(episode.id)
        _state.value = _state.value.copy(resumePositionMs = progress?.positionMs ?: 0)
    }
}
