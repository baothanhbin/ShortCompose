package com.baothanhbin.shortcompose.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.baothanhbin.shortcompose.feature.cinemix.navigation.CINEMIX_ROUTE

@Stable
class AppState(
    val navController: NavHostController,
) {
    @Composable
    fun currentDestination(): NavDestination? {
        val backStackEntry by navController.currentBackStackEntryAsState()
        return backStackEntry?.destination
    }

    fun navigateToTopLevelDestination(destination: TopLevelDestination) {
        navController.navigate(destination.route) {
            // The root graph starts at Splash. Anchor the three main destinations
            // to Cinemix so each tab's back stack and ViewModel can be restored.
            popUpTo(CINEMIX_ROUTE) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun isTopLevelDestinationInHierarchy(destination: TopLevelDestination): Boolean {
        return navController.currentDestination
            ?.hierarchy
            ?.any { it.route == destination.route } == true
    }
}

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
): AppState = remember(navController) {
    AppState(navController = navController)
}
