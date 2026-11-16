package com.baothanhbin.shortcompose.feature.onboarding.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.baothanhbin.shortcompose.feature.cinemix.navigation.CINEMIX_ROUTE
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
    composable(SPLASH_ROUTE) {
        SplashScreen(isAppReady = isAppReady) {
            navController.navigate(LANGUAGE_START_ROUTE) {
                popUpTo(SPLASH_ROUTE) { inclusive = true }
                launchSingleTop = true
            }
        }
    }
    composable(LANGUAGE_START_ROUTE) {
        LanguageScreen(onContinue = { navController.navigate(INTRO_ROUTE) })
    }
    composable(INTRO_ROUTE) {
        IntroScreen(onFinished = { navController.navigate(PERMISSION_ROUTE) })
    }
    composable(
        route = PERMISSION_ROUTE,
        deepLinks = listOf(navDeepLink { uriPattern = "example://app/PermissionActivity" }),
    ) {
        PermissionScreen(onContinue = { navController.navigate(AGE_CONFIRM_ROUTE) })
    }
    composable(AGE_CONFIRM_ROUTE) {
        AgeConfirmScreen(
            onComplete = {
                onOnboardingComplete()
                navController.navigate(CINEMIX_ROUTE) {
                    popUpTo(LANGUAGE_START_ROUTE) { inclusive = true }
                    launchSingleTop = true
                }
            },
            onRestricted = { navController.navigate(AGE_RESTRICTION_ROUTE) },
        )
    }
    composable(AGE_RESTRICTION_ROUTE) {
        AgeRestrictionScreen(onRetry = { navController.popBackStack(AGE_CONFIRM_ROUTE, inclusive = false) })
    }
}
