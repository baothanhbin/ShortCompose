package com.baothanhbin.shortcompose.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.baothanhbin.shortcompose.feature.settings.SettingsRoute
import com.baothanhbin.shortcompose.feature.settings.AboutScreen
import com.baothanhbin.shortcompose.feature.settings.LanguagePreferencesScreen
import com.baothanhbin.shortcompose.feature.settings.PolicyScreen

const val SETTINGS_ROUTE = "settings"
const val LANGUAGE_ROUTE = "language"
const val ABOUT_ROUTE = "about"
const val POLICY_ROUTE = "policy"

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    navigate(
        route = SETTINGS_ROUTE,
        navOptions = navOptions,
    )
}

fun NavGraphBuilder.settingsScreen(onLanguageClick: () -> Unit, onAboutClick: () -> Unit) {
    composable(route = SETTINGS_ROUTE) {
        SettingsRoute(onLanguageClick, onAboutClick)
    }
}

fun NavGraphBuilder.settingsSecondaryScreens(
    onBack: () -> Unit,
    onLanguageSaved: () -> Unit,
    onPolicyClick: () -> Unit,
) {
    composable(LANGUAGE_ROUTE) {
        LanguagePreferencesScreen(onBack = onBack, onSaved = onLanguageSaved)
    }
    composable(ABOUT_ROUTE) {
        AboutScreen(onBack = onBack, onPolicy = onPolicyClick)
    }
    composable(POLICY_ROUTE) {
        PolicyScreen(onBack = onBack)
    }
}
