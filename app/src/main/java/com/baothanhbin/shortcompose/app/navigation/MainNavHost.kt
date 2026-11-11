package com.baothanhbin.shortcompose.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.baothanhbin.shortcompose.R
import com.baothanhbin.shortcompose.core.model.FeatureDestination
import com.baothanhbin.shortcompose.feature.cinemix.navigation.cinemixScreen
import com.baothanhbin.shortcompose.feature.playlist.navigation.playlistScreen
import com.baothanhbin.shortcompose.feature.settings.navigation.settingsScreen
import com.baothanhbin.shortcompose.feature.cinemix.navigation.CINEMIX_ROUTE
import com.baothanhbin.shortcompose.feature.playlist.navigation.PLAYLIST_ROUTE
import com.baothanhbin.shortcompose.feature.settings.navigation.SETTINGS_ROUTE

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = CINEMIX_ROUTE,
        modifier = modifier,
    ) {
        cinemixScreen()
        playlistScreen()
        settingsScreen()
    }
}
