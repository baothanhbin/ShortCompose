package com.baothanhbin.shortcompose.feature.discover.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.baothanhbin.shortcompose.feature.discover.CategoryRoute
import com.baothanhbin.shortcompose.feature.discover.SearchRoute
import com.baothanhbin.shortcompose.feature.discover.SeeAllRoute

const val SEARCH_ROUTE = "search"
const val CATEGORY_ROUTE = "category/{category}"
const val SEE_ALL_ROUTE = "see-all/{type}"

fun NavGraphBuilder.discoverScreens(
    onBack: () -> Unit,
    onMovieClick: (Long) -> Unit,
) {
    composable(SEARCH_ROUTE) {
        SearchRoute(onBack = onBack, onMovieClick = onMovieClick)
    }
    composable(
        route = CATEGORY_ROUTE,
        arguments = listOf(navArgument("category") { type = NavType.StringType }),
    ) {
        CategoryRoute(onBack = onBack, onMovieClick = onMovieClick)
    }
    composable(
        route = SEE_ALL_ROUTE,
        arguments = listOf(navArgument("type") { type = NavType.StringType }),
    ) {
        SeeAllRoute(onBack = onBack, onMovieClick = onMovieClick)
    }
}
