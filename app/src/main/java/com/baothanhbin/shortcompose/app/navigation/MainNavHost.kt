package com.baothanhbin.shortcompose.app.navigation

import android.net.Uri
import android.os.SystemClock
import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navDeepLink
import com.baothanhbin.shortcompose.app.ui.MainTabsContent
import com.baothanhbin.shortcompose.core.navigation.asdBackEnter
import com.baothanhbin.shortcompose.core.navigation.asdBackExit
import com.baothanhbin.shortcompose.core.navigation.asdForwardEnter
import com.baothanhbin.shortcompose.core.navigation.asdForwardExit
import com.baothanhbin.shortcompose.feature.discover.navigation.SEARCH_ROUTE
import com.baothanhbin.shortcompose.feature.discover.navigation.discoverScreens
import com.baothanhbin.shortcompose.feature.offline.navigation.offlineScreen
import com.baothanhbin.shortcompose.feature.onboarding.navigation.onboardingScreens
import com.baothanhbin.shortcompose.feature.video_player.navigation.playerScreen
import com.baothanhbin.shortcompose.feature.settings.navigation.ABOUT_ROUTE
import com.baothanhbin.shortcompose.feature.settings.navigation.LANGUAGE_ROUTE
import com.baothanhbin.shortcompose.feature.settings.navigation.POLICY_ROUTE
import com.baothanhbin.shortcompose.feature.settings.navigation.settingsSecondaryScreens

const val MAIN_ROUTE = "main"

@Composable
fun MainNavHost(
    navController: NavHostController,
    selectedDestination: TopLevelDestination,
    isAppReady: Boolean,
    onOnboardingComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val onBack = { navController.popBackStack(); Unit }
    val onMovieClick: (Long) -> Unit = { navController.navigate("player/$it") }
    val visibleDestination by navController.currentBackStackEntryAsState()
    val showMainAboveNav = visibleDestination?.destination?.route == MAIN_ROUTE
    val currentSelectedDestination by rememberUpdatedState(selectedDestination)
    val currentOnMovieClick by rememberUpdatedState(onMovieClick)
    val mainTabs = remember {
        movableContentOf {
            MainTabsContent(
                selectedDestination = currentSelectedDestination,
                onMovieClick = currentOnMovieClick,
                onSearchClick = { navController.navigate(SEARCH_ROUTE) },
                onCategoryClick = { navController.navigate("category/${Uri.encode(it)}") },
                onSeeAllClick = { navController.navigate("see-all/$it") },
                onLanguageClick = { navController.navigate(LANGUAGE_ROUTE) },
                onAboutClick = { navController.navigate(ABOUT_ROUTE) },
            )
        }
    }

    // Keep the home composition alive underneath secondary destinations. In
    // particular, popping a full-screen player now reveals the already-measured
    // home UI instead of rebuilding its images, pagers, and lists on the main
    // thread during the back gesture.
    Box(modifier) {
        if (!showMainAboveNav) mainTabs()

        NavHost(
            navController = navController,
            startDestination = MAIN_ROUTE,
            modifier = Modifier.fillMaxSize(),
            enterTransition = {
            if (initialState.destination.route == MAIN_ROUTE && targetState.destination.route == MAIN_ROUTE) {
                EnterTransition.None
            } else {
                asdForwardEnter()
            }
        },
        exitTransition = {
            if (initialState.destination.route == MAIN_ROUTE && targetState.destination.route == MAIN_ROUTE) {
                ExitTransition.None
            } else {
                asdForwardExit()
            }
        },
        popEnterTransition = {
            Log.d(
                "NavigationPerf",
                "pop enter ${initialState.destination.route} -> ${targetState.destination.route} at ${SystemClock.elapsedRealtime()}ms",
            )
            if (initialState.destination.route == MAIN_ROUTE && targetState.destination.route == MAIN_ROUTE) {
                EnterTransition.None
            } else {
                asdBackEnter()
            }
        },
        popExitTransition = {
            Log.d(
                "NavigationPerf",
                "pop exit ${initialState.destination.route} -> ${targetState.destination.route} at ${SystemClock.elapsedRealtime()}ms",
            )
            if (initialState.destination.route == MAIN_ROUTE && targetState.destination.route == MAIN_ROUTE) {
                ExitTransition.None
            } else {
                asdBackExit()
            }
            },
        ) {
            // The persistent MainTabsContent above is the visual content for
            // MAIN_ROUTE. Keeping this destination transparent preserves the
            // navigation back stack without recreating the home screen.
            composable(
                route = MAIN_ROUTE,
                deepLinks = listOf(navDeepLink { uriPattern = "example://app/HomeActivity" }),
            ) {
                Box(Modifier.fillMaxSize())
            }
            discoverScreens(onBack = onBack, onMovieClick = onMovieClick)
            playerScreen(onBack = onBack)
            settingsSecondaryScreens(
                onBack = onBack,
                onLanguageSaved = { navController.popBackStack(MAIN_ROUTE, false) },
                onPolicyClick = { navController.navigate(POLICY_ROUTE) },
            )
            offlineScreen()
            onboardingScreens(
                navController = navController,
                isAppReady = isAppReady,
                onOnboardingComplete = onOnboardingComplete,
            )
        }
        if (showMainAboveNav) mainTabs()
    }
}

