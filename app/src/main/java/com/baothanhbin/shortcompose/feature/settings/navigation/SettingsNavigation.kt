package com.baothanhbin.shortcompose.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.baothanhbin.shortcompose.feature.settings.SettingsRoute

const val SETTINGS_ROUTE = "settings"

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    navigate(
        route = SETTINGS_ROUTE,
        navOptions = navOptions,
    )
}

fun NavGraphBuilder.settingsScreen() {
    composable(route = SETTINGS_ROUTE) {
        SettingsRoute()
    }
}
