package com.baothanhbin.shortcompose.feature.discover

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.res.stringResource
import com.baothanhbin.shortcompose.feature.discover.component.MovieGridContent
import com.baothanhbin.shortcompose.feature.discover.component.SearchContent

@Composable
fun SearchRoute(
    onBack: () -> Unit,
    onMovieClick: (Long) -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    SearchContent(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        query = viewModel.query.collectAsStateWithLifecycle().value,
        onQueryChange = viewModel::setQuery,
        onBack = onBack,
        onMovieClick = onMovieClick,
    )
}

@Composable
fun CategoryRoute(
    onBack: () -> Unit,
    onMovieClick: (Long) -> Unit,
    viewModel: CategoryViewModel = hiltViewModel(),
) {
    MovieGridContent(
        title = viewModel.category,
        state = viewModel.state.collectAsStateWithLifecycle().value,
        onBack = onBack,
        onMovieClick = onMovieClick,
    )
}

@Composable
fun SeeAllRoute(
    onBack: () -> Unit,
    onMovieClick: (Long) -> Unit,
    viewModel: SeeAllViewModel = hiltViewModel(),
) {
    MovieGridContent(
        title = stringResource(viewModel.titleRes),
        state = viewModel.state.collectAsStateWithLifecycle().value,
        onBack = onBack,
        onMovieClick = onMovieClick,
    )
}
