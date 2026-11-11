package com.baothanhbin.shortcompose.feature.cinemix

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CinemixUiState(
    val title: String = "",
)

@HiltViewModel
class CinemixViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(
        CinemixUiState(title = ""),
    )
    val uiState: StateFlow<CinemixUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = CinemixUiState(title = "Cinemix")
    }
}
