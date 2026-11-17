package com.baothanhbin.shortcompose.feature.video_player.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.baothanhbin.shortcompose.core.navigation.asdBackExit
import com.baothanhbin.shortcompose.feature.video_player.PlayerRoute

const val PLAYER_ROUTE = "player/{movieId}"

fun NavGraphBuilder.playerScreen(onBack: () -> Unit) {
    composable(
        route = PLAYER_ROUTE,
        arguments = listOf(navArgument("movieId") { type = NavType.LongType }),
        // Reverse the forward transition: player slides right while the previous
        // screen enters from the left.
        popExitTransition = { asdBackExit() },
    ) {
        PlayerRoute(onBack = onBack)
    }
}
