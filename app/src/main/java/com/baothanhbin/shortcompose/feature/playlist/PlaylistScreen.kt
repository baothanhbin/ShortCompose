package com.baothanhbin.shortcompose.feature.playlist

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baothanhbin.shortcompose.feature.playlist.component.PlaylistContent

@Composable
fun PlaylistRoute(
    onMovieClick: (Long) -> Unit,
    viewModel: PlaylistViewModel = hiltViewModel(),
) {
    PlaylistContent(
        state = viewModel.uiState.collectAsStateWithLifecycle().value,
        onTabClick = viewModel::selectTab,
        onMovieClick = onMovieClick,
    )
}
