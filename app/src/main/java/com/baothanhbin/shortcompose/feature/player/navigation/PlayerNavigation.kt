package com.baothanhbin.shortcompose.feature.player.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.baothanhbin.shortcompose.feature.player.PlayerRoute

const val PLAYER_ROUTE = "player/{movieId}"

fun NavGraphBuilder.playerScreen(onBack: () -> Unit) {
    composable(
        route = PLAYER_ROUTE,
        arguments = listOf(navArgument("movieId") { type = NavType.LongType }),
    ) {
        PlayerRoute(onBack = onBack)
    }
}
