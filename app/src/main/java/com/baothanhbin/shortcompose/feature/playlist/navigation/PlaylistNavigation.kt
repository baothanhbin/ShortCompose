package com.baothanhbin.shortcompose.feature.playlist.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.baothanhbin.shortcompose.feature.playlist.PlaylistRoute

const val PLAYLIST_ROUTE = "playlist"
fun NavGraphBuilder.playlistScreen(onMovieClick: (Long) -> Unit) {
    composable(PLAYLIST_ROUTE) { PlaylistRoute(onMovieClick) }
}
