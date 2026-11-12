package com.baothanhbin.shortcompose.app.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.baothanhbin.shortcompose.R
import com.baothanhbin.shortcompose.feature.cinemix.navigation.CINEMIX_ROUTE
import com.baothanhbin.shortcompose.feature.playlist.navigation.PLAYLIST_ROUTE
import com.baothanhbin.shortcompose.feature.settings.navigation.SETTINGS_ROUTE

enum class TopLevelDestination(
    val route: String,
    @DrawableRes val selectedIconId: Int,
    @DrawableRes val unselectedIconId: Int,
    @StringRes val labelId: Int,
) {
    CINEMIX(
        route = CINEMIX_ROUTE,
        selectedIconId = R.drawable.ic_cinemix_movie,
        unselectedIconId = R.drawable.ic_cinemix_movie,
        labelId = R.string.feature_cinemix_title,
    ),
    PLAYLIST(
        route = PLAYLIST_ROUTE,
        selectedIconId = R.drawable.ic_cinemix_playlist,
        unselectedIconId = R.drawable.ic_cinemix_playlist,
        labelId = R.string.feature_playlist_title,
    ),
    SETTINGS(
        route = SETTINGS_ROUTE,
        selectedIconId = R.drawable.ic_cinemix_settings,
        unselectedIconId = R.drawable.ic_cinemix_settings,
        labelId = R.string.feature_settings_title,
    ),
}
