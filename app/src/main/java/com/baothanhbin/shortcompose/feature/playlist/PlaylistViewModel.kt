package com.baothanhbin.shortcompose.feature.playlist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class PlaylistUiState(
    val title: String = "",
)

@HiltViewModel
class PlaylistViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(
        PlaylistUiState(title = ""),
    )
    val uiState: StateFlow<PlaylistUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = PlaylistUiState(title = "Playlist")
    }
}
