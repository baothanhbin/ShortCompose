package com.baothanhbin.shortcompose.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baothanhbin.shortcompose.feature.settings.component.SettingsContent

@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    SettingsScreen(
        title = uiState.title,
    )
}

@Composable
fun SettingsScreen(
    title: String,
    modifier: Modifier = Modifier,
) {
    SettingsContent(
        title = title,
        modifier = modifier,
    )
}
