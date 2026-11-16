package com.baothanhbin.shortcompose.feature.settings

import androidx.lifecycle.ViewModel
import androidx.annotation.StringRes
import com.baothanhbin.shortcompose.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SettingsUiState(
    @StringRes val titleRes: Int = R.string.settings_title,
)

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(
        SettingsUiState(),
    )
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = SettingsUiState()
    }
}
