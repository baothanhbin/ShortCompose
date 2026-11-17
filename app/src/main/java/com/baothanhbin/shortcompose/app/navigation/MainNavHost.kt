package com.baothanhbin.shortcompose.app.navigation

import android.net.Uri
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.baothanhbin.shortcompose.core.navigation.asdBackEnter
import com.baothanhbin.shortcompose.core.navigation.asdBackExit
import com.baothanhbin.shortcompose.core.navigation.asdForwardEnter
import com.baothanhbin.shortcompose.core.navigation.asdForwardExit
import com.baothanhbin.shortcompose.feature.cinemix.navigation.CINEMIX_ROUTE
import com.baothanhbin.shortcompose.feature.cinemix.navigation.cinemixScreen
import com.baothanhbin.shortcompose.feature.discover.navigation.SEARCH_ROUTE
import com.baothanhbin.shortcompose.feature.discover.navigation.discoverScreens
import com.baothanhbin.shortcompose.feature.offline.navigation.offlineScreen
import com.baothanhbin.shortcompose.feature.onboarding.navigation.onboardingScreens
import com.baothanhbin.shortcompose.feature.player.navigation.playerScreen
import com.baothanhbin.shortcompose.feature.playlist.navigation.playlistScreen
import com.baothanhbin.shortcompose.feature.playlist.navigation.PLAYLIST_ROUTE
import com.baothanhbin.shortcompose.feature.settings.navigation.ABOUT_ROUTE
import com.baothanhbin.shortcompose.feature.settings.navigation.LANGUAGE_ROUTE
import com.baothanhbin.shortcompose.feature.settings.navigation.POLICY_ROUTE
import com.baothanhbin.shortcompose.feature.settings.navigation.settingsScreen
import com.baothanhbin.shortcompose.feature.settings.navigation.settingsSecondaryScreens
import com.baothanhbin.shortcompose.feature.settings.navigation.SETTINGS_ROUTE

private val topLevelRoutes = setOf(CINEMIX_ROUTE, PLAYLIST_ROUTE, SETTINGS_ROUTE)

@Composable
fun MainNavHost(
    navController: NavHostController,
    isAppReady: Boolean,
    onOnboardingComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val onBack = { navController.popBackStack(); Unit }
    val onMovieClick: (Long) -> Unit = { navController.navigate("player/$it") }

    NavHost(
        navController = navController,
        startDestination = CINEMIX_ROUTE,
        modifier = modifier,
        enterTransition = {
            if (initialState.destination.route in topLevelRoutes && targetState.destination.route in topLevelRoutes) {
                EnterTransition.None
            } else {
                asdForwardEnter()
            }
        },
        exitTransition = {
            if (initialState.destination.route in topLevelRoutes && targetState.destination.route in topLevelRoutes) {
                ExitTransition.None
            } else {
                asdForwardExit()
            }
        },
        popEnterTransition = {
            if (initialState.destination.route in topLevelRoutes && targetState.destination.route in topLevelRoutes) {
                EnterTransition.None
            } else {
                asdBackEnter()
            }
        },
        popExitTransition = {
            if (initialState.destination.route in topLevelRoutes && targetState.destination.route in topLevelRoutes) {
                ExitTransition.None
            } else {
                asdBackExit()
            }
        },
    ) {
        cinemixScreen(
            onMovieClick = onMovieClick,
            onSearchClick = { navController.navigate(SEARCH_ROUTE) },
            onCategoryClick = { navController.navigate("category/${Uri.encode(it)}") },
            onSeeAllClick = { navController.navigate("see-all/$it") },
        )
        playlistScreen(onMovieClick = onMovieClick)
        settingsScreen(
            onLanguageClick = { navController.navigate(LANGUAGE_ROUTE) },
            onAboutClick = { navController.navigate(ABOUT_ROUTE) },
        )
        discoverScreens(onBack = onBack, onMovieClick = onMovieClick)
        playerScreen(onBack = onBack)
        settingsSecondaryScreens(
            onBack = onBack,
            onLanguageSaved = { navController.popBackStack(CINEMIX_ROUTE, false) },
            onPolicyClick = { navController.navigate(POLICY_ROUTE) },
        )
        offlineScreen()
        onboardingScreens(
            navController = navController,
            isAppReady = isAppReady,
            onOnboardingComplete = onOnboardingComplete,
        )
    }
}
