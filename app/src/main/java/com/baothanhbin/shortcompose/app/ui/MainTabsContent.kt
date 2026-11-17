package com.baothanhbin.shortcompose.app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.baothanhbin.shortcompose.app.navigation.TopLevelDestination
import com.baothanhbin.shortcompose.feature.cinemix.CinemixRoute
import com.baothanhbin.shortcompose.feature.playlist.PlaylistRoute
import com.baothanhbin.shortcompose.feature.settings.SettingsRoute

/** Keeps all three primary screens composed, matching ASD113's offscreenPageLimit = 2. */
@Composable
fun MainTabsContent(
    selectedDestination: TopLevelDestination,
    onMovieClick: (Long) -> Unit,
    onSearchClick: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onSeeAllClick: (String) -> Unit,
    onLanguageClick: () -> Unit,
    onAboutClick: () -> Unit,
) {
    val destinations = TopLevelDestination.entries
    val selectedPage = destinations.indexOf(selectedDestination).coerceAtLeast(0)
    val pagerState = rememberPagerState(initialPage = selectedPage) { destinations.size }

    LaunchedEffect(selectedPage) {
        if (pagerState.currentPage != selectedPage) {
            pagerState.scrollToPage(selectedPage)
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        userScrollEnabled = false,
        beyondViewportPageCount = destinations.lastIndex,
        key = { page -> destinations[page].route },
    ) { page ->
        when (destinations[page]) {
            TopLevelDestination.CINEMIX -> CinemixRoute(
                onMovieClick = onMovieClick,
                onSearchClick = onSearchClick,
                onCategoryClick = onCategoryClick,
                onSeeAllClick = onSeeAllClick,
            )

            TopLevelDestination.PLAYLIST -> PlaylistRoute(onMovieClick = onMovieClick)

            TopLevelDestination.SETTINGS -> SettingsRoute(
                onLanguageClick = onLanguageClick,
                onAboutClick = onAboutClick,
            )
        }
    }
}
