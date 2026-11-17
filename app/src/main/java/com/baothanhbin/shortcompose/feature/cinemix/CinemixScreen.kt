package com.baothanhbin.shortcompose.feature.cinemix

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baothanhbin.shortcompose.feature.cinemix.component.CinemixContent

@Composable
fun CinemixRoute(
    onMovieClick: (Long) -> Unit,
    onSearchClick: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onSeeAllClick: (String) -> Unit,
    viewModel: CinemixViewModel = hiltViewModel(),
) {
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
