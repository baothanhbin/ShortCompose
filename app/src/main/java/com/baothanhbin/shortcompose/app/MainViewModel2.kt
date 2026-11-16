package com.baothanhbin.shortcompose.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baothanhbin.shortcompose.core.datastore.AppSettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MainUiState(val isReady: Boolean = false, val onboardingCompleted: Boolean = false)

@HiltViewModel
class MainViewModel @Inject constructor(private val settings: AppSettingsDataStore) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settings.appSettings.collect { _uiState.value = MainUiState(true, it.onboardingCompleted) }
        }
    }

    fun completeOnboarding() = viewModelScope.launch { settings.updateOnboardingCompleted(true) }
}
