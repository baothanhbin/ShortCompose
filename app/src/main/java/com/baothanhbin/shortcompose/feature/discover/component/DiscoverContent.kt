package com.baothanhbin.shortcompose.feature.discover.component

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.baothanhbin.shortcompose.core.ui.component.MovieCard
import com.baothanhbin.shortcompose.R
import com.baothanhbin.shortcompose.core.utils.drawableBackground
import com.baothanhbin.shortcompose.feature.discover.MovieListUiState
import com.baothanhbin.shortcompose.core.ui.preview.PreviewTheme
import com.baothanhbin.shortcompose.core.ui.preview.previewMovies
import com.baothanhbin.shortcompose.core.utils.Tap
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
@OptIn(FlowPreview::class)
fun SearchContent(
    state: MovieListUiState,
    onQueryChanged: (String) -> Unit,
    onBack: () -> Unit,
    onMovieClick: (Long) -> Unit,
) {
    val searchFieldState = rememberTextFieldState()

    LaunchedEffect(searchFieldState) {
        snapshotFlow { searchFieldState.text.toString() }
            .distinctUntilChanged()
            .debounce(400)
            .collect(onQueryChanged)
    }

    MovieGridContent(
        state = state,
        onBack = onBack,
        onMovieClick = onMovieClick,
        topBar = {
            SearchTopBar(
                searchFieldState = searchFieldState,
                onBack = onBack,
            )
        },
    )
}

@Composable
fun MovieGridContent(
    state: MovieListUiState,
    onBack: () -> Unit,
    onMovieClick: (Long) -> Unit,
    title: String = "",
    topBar: @Composable (() -> Unit)? = null,
) = Column(
    Modifier
        .fillMaxSize()
        .paint(painterResource(R.drawable.bg_main), contentScale = ContentScale.Crop)
) {
    (topBar ?: {
        CategoryTopBar(title = title, onBack = onBack)
    })()
    Box(
        Modifier
            .weight(1f)
            .fillMaxWidth()
    ) {
        when {
            state.loading -> LazyVerticalGrid(
                GridCells.Fixed(3),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(9) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(184.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(.09f))
                    )
                }
            }

            state.error != null || state.errorRes != null -> Text(
                state.error ?: stringResource(state.errorRes!!),
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )

            state.movies.isEmpty() -> Column(
                Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painterResource(R.drawable.iv_search_empty), null, Modifier.size(231.dp))
                Text(
                    stringResource(R.string.search_empty_title),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    stringResource(R.string.search_empty_message),
                    color = Color.White.copy(.6f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            else -> LazyVerticalGrid(
                GridCells.Fixed(3),
                contentPadding = PaddingValues(12.dp, 12.dp, 12.dp, 24.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(state.movies, key = { it.id }) { movie ->
                    MovieCard(
                        movie, { onMovieClick(movie.id) }, Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryTopBar(title: String, onBack: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 35.dp, start = 12.dp, end = 12.dp)
            .height(64.dp)
            .paint(
                painterResource(R.drawable.bg_top_search), contentScale = ContentScale.FillBounds
            ), verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.iv_back),
            contentDescription = stringResource(R.string.action_back),
            modifier = Modifier
                .padding(start = 8.dp) // Đặt padding TRƯỚC size để không bóp ảnh
                .size(48.dp)          // Chỉnh kích thước vòng tròn to/nhỏ tùy ý tại đây
                .clip(CircleShape)
                .Tap(onClick = onBack),
            contentScale = ContentScale.Fit
        )
        Text(
            title,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun SearchTopBar(
    searchFieldState: TextFieldState,
    onBack: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 35.dp, start = 12.dp, end = 12.dp)
            // TextField is 56dp high and has 8dp outer padding on each side.
            // The parent must reserve 72dp; otherwise Row constrains and clips it.
            .height(72.dp)
            .paint(
                painterResource(R.drawable.bg_top_search), contentScale = ContentScale.FillBounds
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(R.drawable.iv_back),
            contentDescription = stringResource(R.string.action_back),
            modifier = Modifier
                .padding(start = 8.dp)
                .size(48.dp)
                .clip(CircleShape)
                .Tap(onClick = onBack),
            contentScale = ContentScale.Fit,
        )
        OutlinedTextField(
            state = searchFieldState,
            lineLimits = TextFieldLineLimits.SingleLine,
            placeholder = { Text(stringResource(R.string.search_title)) },
            trailingIcon = { Image(painterResource(R.drawable.ic_search), null) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedPlaceholderColor = Color.White.copy(alpha = .45f),
                unfocusedPlaceholderColor = Color.White.copy(alpha = .45f),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color.White,
            ),
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp, end = 10.dp, top = 8.dp, bottom = 8.dp)
                .height(56.dp)
                .drawableBackground(R.drawable.bg_search_field),
        )
    }
}

@Preview(name = "Search", showBackground = true, showSystemUi = true)
@Composable
private fun SearchContentPreview() = PreviewTheme {
    SearchContent(
        state = MovieListUiState(loading = false, movies = previewMovies),
        onQueryChanged = {},
        onBack = {},
        onMovieClick = {},
    )
}

@Preview(name = "Category", showBackground = true)
@Composable
private fun MovieGridContentPreview() = PreviewTheme {
    MovieGridContent(
        title = stringResource(R.string.category_romance),
        state = MovieListUiState(loading = false, movies = previewMovies),
        onBack = {},
        onMovieClick = {},
    )
}

@Preview(name = "Search - Empty", showBackground = true)
@Composable
private fun MovieGridEmptyPreview() = PreviewTheme {
    MovieGridContent(
        title = stringResource(R.string.search_title),
        state = MovieListUiState(loading = false),
        onBack = {},
        onMovieClick = {},
    )
}

@Preview(name = "See all - Trending", showBackground = true)
@Composable
private fun SeeAllContentPreview() = PreviewTheme {
    MovieGridContent(
        title = stringResource(R.string.cinemix_trending_now),
        state = MovieListUiState(loading = false, movies = previewMovies),
        onBack = {},
        onMovieClick = {},
    )
}
