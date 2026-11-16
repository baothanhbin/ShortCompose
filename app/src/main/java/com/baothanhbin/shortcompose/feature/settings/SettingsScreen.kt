package com.baothanhbin.shortcompose.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baothanhbin.shortcompose.feature.settings.component.SettingsContent

@Composable
fun SettingsRoute(
    onLanguageClick: () -> Unit,
    onAboutClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    SettingsScreen(
        title = stringResource(uiState.titleRes),
        onLanguageClick = onLanguageClick,
        onAboutClick = onAboutClick,
    )
}

@Composable
fun SettingsScreen(
    title: String,
    onLanguageClick: () -> Unit,
    onAboutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingsContent(
        title = title,
        onLanguageClick = onLanguageClick,
        onAboutClick = onAboutClick,
        modifier = modifier,
    )
}
