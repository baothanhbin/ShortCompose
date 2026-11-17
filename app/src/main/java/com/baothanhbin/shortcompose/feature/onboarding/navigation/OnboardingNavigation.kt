package com.baothanhbin.shortcompose.feature.onboarding.navigation

import androidx.compose.animation.EnterTransition
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.baothanhbin.shortcompose.core.navigation.asdBackEnter
import com.baothanhbin.shortcompose.core.navigation.asdBackExit
import com.baothanhbin.shortcompose.core.navigation.asdForwardEnter
import com.baothanhbin.shortcompose.core.navigation.asdForwardExit
import com.baothanhbin.shortcompose.app.navigation.MAIN_ROUTE
import com.baothanhbin.shortcompose.feature.onboarding.AgeConfirmScreen
import com.baothanhbin.shortcompose.feature.onboarding.AgeRestrictionScreen
import com.baothanhbin.shortcompose.feature.onboarding.IntroScreen
import com.baothanhbin.shortcompose.feature.onboarding.LanguageScreen
import com.baothanhbin.shortcompose.feature.onboarding.PermissionScreen
import com.baothanhbin.shortcompose.feature.onboarding.SplashScreen

const val SPLASH_ROUTE = "splash"
const val LANGUAGE_START_ROUTE = "onboarding/language"
const val INTRO_ROUTE = "onboarding/intro"
const val PERMISSION_ROUTE = "permission"
const val AGE_CONFIRM_ROUTE = "onboarding/age-confirm"
const val AGE_RESTRICTION_ROUTE = "onboarding/age-restriction"

fun NavGraphBuilder.onboardingScreens(
    navController: NavHostController,
    isAppReady: Boolean,
    onOnboardingComplete: () -> Unit,
) {
    composable(
        route = SPLASH_ROUTE,
        enterTransition = { EnterTransition.None },
        exitTransition = { asdForwardExit() },
    ) {
        SplashScreen(isAppReady = isAppReady) {
            navController.navigate(LANGUAGE_START_ROUTE) {
                launchSingleTop = true
            }
        }
    }
    composable(
        route = LANGUAGE_START_ROUTE,
        enterTransition = { asdForwardEnter() },
        exitTransition = { asdForwardExit() },
        popEnterTransition = { asdBackEnter() },
        popExitTransition = { asdBackExit() },
    ) {
        LanguageScreen(onContinue = { navController.navigate(INTRO_ROUTE) })
    }
    composable(
        route = INTRO_ROUTE,
        enterTransition = { asdForwardEnter() },
        exitTransition = { asdForwardExit() },
        popEnterTransition = { asdBackEnter() },
        popExitTransition = { asdBackExit() },
    ) {
        IntroScreen(onFinished = { navController.navigate(PERMISSION_ROUTE) })
    }
    composable(
        route = PERMISSION_ROUTE,
        deepLinks = listOf(navDeepLink { uriPattern = "example://app/PermissionActivity" }),
        enterTransition = { asdForwardEnter() },
        exitTransition = { asdForwardExit() },
        popEnterTransition = { asdBackEnter() },
        popExitTransition = { asdBackExit() },
    ) {
        PermissionScreen(onContinue = { navController.navigate(AGE_CONFIRM_ROUTE) })
    }
    composable(
        route = AGE_CONFIRM_ROUTE,
        enterTransition = { asdForwardEnter() },
        exitTransition = { asdForwardExit() },
        popEnterTransition = { asdBackEnter() },
        popExitTransition = { asdBackExit() },
    ) {
        AgeConfirmScreen(
            onComplete = {
                onOnboardingComplete()
                navController.navigate(MAIN_ROUTE) {
                    // Keep the startup destinations alive while their slide transitions
                    // run, then clear the entire flow only after entering the main app.
                    popUpTo(SPLASH_ROUTE) { inclusive = true }
                    launchSingleTop = true
                }
            },
            onRestricted = { navController.navigate(AGE_RESTRICTION_ROUTE) },
        )
    }
    composable(
        route = AGE_RESTRICTION_ROUTE,
        enterTransition = { asdForwardEnter() },
        exitTransition = { asdForwardExit() },
        popEnterTransition = { asdBackEnter() },
        popExitTransition = { asdBackExit() },
    ) {
        AgeRestrictionScreen(onRetry = { navController.popBackStack(AGE_CONFIRM_ROUTE, inclusive = false) })
    }
}
