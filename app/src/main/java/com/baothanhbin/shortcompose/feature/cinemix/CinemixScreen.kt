package com.baothanhbin.shortcompose.feature.cinemix

import androidx.compose.runtime.Composable
import androidx.activity.compose.LocalActivity
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.ViewModelStoreOwner
import com.baothanhbin.shortcompose.feature.cinemix.component.CinemixContent

@Composable
fun CinemixRoute(
    onMovieClick: (Long) -> Unit,
    onSearchClick: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onSeeAllClick: (String) -> Unit,
) {
    // ASD113 leaves MainActivity alive behind VideoPlayerActivity. Scope this
    // state to our single activity for the same behavior: returning from the
    // player reuses loaded home content instead of showing the skeleton again.
    val activityOwner = LocalActivity.current as? ViewModelStoreOwner ?: return
    val viewModel: CinemixViewModel = hiltViewModel(viewModelStoreOwner = activityOwner)
    CinemixContent(
        state = viewModel.uiState.collectAsStateWithLifecycle().value,
        onRetry = viewModel::load,
        onMovieClick = onMovieClick,
        onFavoriteClick = viewModel::toggleFavorite,
        onSearchClick = onSearchClick,
        onCategoryClick = onCategoryClick,
        onSeeAllClick = onSeeAllClick,
    )
}
