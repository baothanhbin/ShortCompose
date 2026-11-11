package com.baothanhbin.shortcompose.feature.cinemix.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.baothanhbin.shortcompose.feature.cinemix.CinemixRoute

const val CINEMIX_ROUTE = "cinemix"

fun NavController.navigateToCinemix(navOptions: NavOptions? = null) {
    navigate(
        route = CINEMIX_ROUTE,
        navOptions = navOptions,
    )
}

fun NavGraphBuilder.cinemixScreen() {
    composable(route = CINEMIX_ROUTE) {
        CinemixRoute()
    }
}
