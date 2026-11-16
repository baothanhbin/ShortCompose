package com.baothanhbin.shortcompose.feature.discover

import androidx.lifecycle.SavedStateHandle
import androidx.annotation.StringRes
import com.baothanhbin.shortcompose.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baothanhbin.shortcompose.core.data.repository.MovieRepository
import com.baothanhbin.shortcompose.core.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MovieListUiState(
    val loading: Boolean = true,
    val movies: List<Movie> = emptyList(),
    val error: String? = null,
    @StringRes val errorRes: Int? = null,
)

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()
    private val _state = MutableStateFlow(MovieListUiState())
    val state = _state.asStateFlow()
    private var searchJob: Job? = null

    init { search("") }
    fun setQuery(value: String) { _query.value = value; search(value) }
    private fun search(value: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (value.isNotBlank()) delay(400)
            _state.value = _state.value.copy(loading = true, error = null)
            runCatching { repository.searchMovies(value.trim()) }
                .onSuccess { _state.value = MovieListUiState(false, it) }
                .onFailure { _state.value = MovieListUiState(false, error = it.message, errorRes = R.string.movie_error_search) }
        }
    }
}

@HiltViewModel
class CategoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: MovieRepository,
) : ViewModel() {
    val category: String = savedStateHandle["category"] ?: ""
    private val _state = MutableStateFlow(MovieListUiState())
    val state = _state.asStateFlow()
    init { load() }
    fun load() = viewModelScope.launch {
        _state.value = MovieListUiState()
        runCatching { repository.getMoviesByCategory(category) }
            .onSuccess { _state.value = MovieListUiState(false, it) }
            .onFailure { _state.value = MovieListUiState(false, error = it.message, errorRes = R.string.movie_error_load) }
    }
}

@HiltViewModel
class SeeAllViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: MovieRepository,
) : ViewModel() {
    val type: String = savedStateHandle["type"] ?: "trending"
    @StringRes val titleRes: Int = when (type) {
        "new" -> R.string.cinemix_new_releases
        "spotlight" -> R.string.cinemix_spotlight
        else -> R.string.cinemix_trending_now
    }
    private val _state = MutableStateFlow(MovieListUiState())
    val state = _state.asStateFlow()
    init { load() }
    fun load() = viewModelScope.launch {
        _state.value = MovieListUiState()
        runCatching {
            when (type) {
                "new" -> repository.getNewReleases()
                "spotlight" -> repository.getSpotlight()
                else -> repository.getTrending()
            }
        }.onSuccess { _state.value = MovieListUiState(false, it) }
            .onFailure { _state.value = MovieListUiState(false, error = it.message, errorRes = R.string.movie_error_load) }
    }
}
