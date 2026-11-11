package com.baothanhbin.shortcompose.feature.playlist

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baothanhbin.shortcompose.feature.playlist.component.PlaylistContent

@Composable
fun PlaylistRoute(
    viewModel: PlaylistViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    PlaylistScreen(
        title = uiState.title,
    )
}

@Composable
fun PlaylistScreen(
    title: String,
    modifier: Modifier = Modifier,
) {
    PlaylistContent(
        title = title,
        modifier = modifier,
    )
}
