package com.baothanhbin.shortcompose.feature.playlist.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.baothanhbin.shortcompose.feature.playlist.PlaylistRoute

const val PLAYLIST_ROUTE = "playlist"

fun NavController.navigateToPlaylist(navOptions: NavOptions? = null) {
    navigate(
        route = PLAYLIST_ROUTE,
        navOptions = navOptions,
    )
}

fun NavGraphBuilder.playlistScreen() {
    composable(route = PLAYLIST_ROUTE) {
        PlaylistRoute()
    }
}
