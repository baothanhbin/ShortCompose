package com.baothanhbin.shortcompose.feature.video_player

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baothanhbin.shortcompose.feature.video_player.component.PlayerContent

@Composable
fun PlayerRoute(
    onBack: () -> Unit,
    viewModel: PlayerViewModel = hiltViewModel(),
) {
    PlayerContent(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        onBack = onBack,
        onEpisode = viewModel::selectEpisode,
        onFavorite = viewModel::toggleFavorite,
        onWatched = viewModel::markWatched,
        onSaveProgress = viewModel::saveProgress,
    )
}
