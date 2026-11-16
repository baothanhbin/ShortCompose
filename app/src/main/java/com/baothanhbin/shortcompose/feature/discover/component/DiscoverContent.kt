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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.baothanhbin.shortcompose.core.ui.drawableBackground
import com.baothanhbin.shortcompose.feature.discover.MovieListUiState
import com.baothanhbin.shortcompose.core.ui.preview.PreviewTheme
import com.baothanhbin.shortcompose.core.ui.preview.previewMovies

@Composable
fun SearchContent(
    state: MovieListUiState,
    query: String,
    onQueryChange: (String) -> Unit,
    onBack: () -> Unit,
    onMovieClick: (Long) -> Unit,
) {
    MovieGridContent(
        title = stringResource(R.string.search_title), state = state, onBack = onBack, onMovieClick = onMovieClick,
        header = {
            OutlinedTextField(
                value = query, onValueChange = onQueryChange, singleLine = true,
                placeholder = { Text(stringResource(R.string.search_hint)) },
                leadingIcon = { Image(painterResource(R.drawable.ic_search), null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .drawableBackground(if (query.isBlank()) R.drawable.bg_search_field else R.drawable.bg_search_field_active),
            )
        },
    )
}

@Composable
fun MovieGridContent(
    title: String,
    state: MovieListUiState,
    onBack: () -> Unit,
    onMovieClick: (Long) -> Unit,
    header: @Composable () -> Unit = {},
) = Column(
    Modifier
        .fillMaxSize()
        .paint(painterResource(R.drawable.bg_main), contentScale = ContentScale.Crop)
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 12.dp, end = 12.dp)
            .height(64.dp)
            .paint(
                painterResource(R.drawable.bg_top_search), contentScale = ContentScale.FillBounds
            ), verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Image(painterResource(R.drawable.iv_back), stringResource(R.string.action_back))
        }
        Text(title, color = Color.White, style = MaterialTheme.typography.titleMedium)
    }
    header()
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
                state.error ?: stringResource(state.errorRes!!), color = Color.White, modifier = Modifier.align(Alignment.Center)
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

@Preview(name = "Search", showBackground = true, showSystemUi = true)
@Composable
private fun SearchContentPreview() = PreviewTheme {
    SearchContent(
        state = MovieListUiState(loading = false, movies = previewMovies),
        query = "love",
        onQueryChange = {},
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
