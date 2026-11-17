package com.baothanhbin.shortcompose.feature.playlist.component

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.baothanhbin.shortcompose.core.ui.component.MovieCard
import com.baothanhbin.shortcompose.R
import com.baothanhbin.shortcompose.core.utils.Tap
import com.baothanhbin.shortcompose.feature.playlist.PlaylistTab
import com.baothanhbin.shortcompose.feature.playlist.PlaylistUiState
import com.baothanhbin.shortcompose.core.ui.preview.PreviewTheme
import com.baothanhbin.shortcompose.core.ui.preview.previewMovies

@Composable
fun PlaylistContent(
    state: PlaylistUiState,
    onTabClick: (PlaylistTab) -> Unit,
    onMovieClick: (Long) -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .paint(painterResource(R.drawable.bg_main), contentScale = ContentScale.Crop)
            .padding(top = 35.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .height(64.dp)
                .paint(painterResource(R.drawable.bg_top), contentScale = ContentScale.FillBounds)
                .padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Tab(
                stringResource(R.string.playlist_recently_watched),
                state.selectedTab == PlaylistTab.RECENTLY_WATCHED,
                Modifier.weight(1f)
            ) { onTabClick(PlaylistTab.RECENTLY_WATCHED) }
            Tab(
                stringResource(R.string.playlist_favorites),
                state.selectedTab == PlaylistTab.FAVORITES,
                Modifier.weight(1f)
            ) { onTabClick(PlaylistTab.FAVORITES) }
        }
        if (state.movies.isEmpty()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(bottom = 96.dp), contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painterResource(R.drawable.iv_age_restriction), null, Modifier.size(231.dp)
                    )
                    Text(
                        stringResource(R.string.playlist_empty_title),
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        stringResource(R.string.playlist_empty_message),
                        color = Color.White.copy(.65f),
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }
        } else LazyVerticalGrid(
            GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 112.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(state.movies, key = { "${it.id}:${it.resumeEpisodeId}" }) { movie ->
                MovieCard(
                    movie = movie,
                    onClick = { onMovieClick(movie.id) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun Tab(text: String, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier
            .fillMaxSize()
            .paint(
                painterResource(if (selected) R.drawable.bg_playlist_selected else R.drawable.bg_playlist_unselected),
                contentScale = ContentScale.FillBounds
            )
            .Tap(onClick = onClick), contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            color = if (selected) Color(0xFFEA7B71) else Color.White,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Preview(name = "Playlist - Recently watched", showBackground = true, showSystemUi = true)
@Composable
private fun PlaylistContentPreview() = PreviewTheme {
    PlaylistContent(
        state = PlaylistUiState(movies = previewMovies),
        onTabClick = {},
        onMovieClick = {},
    )
}

@Preview(name = "Playlist - Empty", showBackground = true)
@Composable
private fun PlaylistEmptyPreview() = PreviewTheme {
    PlaylistContent(state = PlaylistUiState(), onTabClick = {}, onMovieClick = {})
}
