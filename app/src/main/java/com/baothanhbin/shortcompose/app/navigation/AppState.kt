package com.baothanhbin.shortcompose.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Stable
class AppState(
    val navController: NavHostController,
) {
    @Composable
    fun currentDestination(): NavDestination? {
        val backStackEntry by navController.currentBackStackEntryAsState()
        return backStackEntry?.destination
    }

}

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
): AppState = remember(navController) {
    AppState(navController = navController)
}
