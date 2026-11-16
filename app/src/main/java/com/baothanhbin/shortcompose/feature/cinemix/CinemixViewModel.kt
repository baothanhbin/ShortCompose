package com.baothanhbin.shortcompose.feature.cinemix

import androidx.lifecycle.ViewModel
import androidx.annotation.StringRes
import com.baothanhbin.shortcompose.R
import androidx.lifecycle.viewModelScope
import com.baothanhbin.shortcompose.core.data.repository.MovieRepository
import com.baothanhbin.shortcompose.core.model.HomeContent
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
class CinemixViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(CinemixUiState())
    val uiState: StateFlow<CinemixUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        runCatching { repository.getHomeContent() }
            .onSuccess { _uiState.value = CinemixUiState(content = it, isLoading = false) }
            .onFailure { _uiState.value = CinemixUiState(isLoading = false, error = it.message, errorRes = R.string.movie_error_load) }
    }
}
