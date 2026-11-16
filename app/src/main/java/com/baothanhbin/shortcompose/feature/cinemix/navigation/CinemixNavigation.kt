package com.baothanhbin.shortcompose.feature.cinemix.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.baothanhbin.shortcompose.feature.cinemix.CinemixRoute

const val CINEMIX_ROUTE = "cinemix"

fun NavGraphBuilder.cinemixScreen(
    onMovieClick: (Long) -> Unit,
    onSearchClick: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onSeeAllClick: (String) -> Unit,
) {
    composable(CINEMIX_ROUTE, deepLinks = listOf(navDeepLink { uriPattern = "example://app/HomeActivity" })) {
        CinemixRoute(onMovieClick, onSearchClick, onCategoryClick, onSeeAllClick)
    }
}
