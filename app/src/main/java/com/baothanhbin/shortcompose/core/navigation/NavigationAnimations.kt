package com.baothanhbin.shortcompose.core.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

private const val ASD_SCREEN_TRANSITION_MS = 400
/**
 * Matches ASD113's `startNextActivity`: the next screen enters from the right.
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.asdForwardEnter(): EnterTransition =
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(ASD_SCREEN_TRANSITION_MS),
    )

/** Matches ASD113's `startNextActivity`: the current screen exits to the left. */
fun AnimatedContentTransitionScope<NavBackStackEntry>.asdForwardExit(): ExitTransition =
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(ASD_SCREEN_TRANSITION_MS),
    )

/** Matches ASD113's back animation: the previous screen enters from the left. */
fun AnimatedContentTransitionScope<NavBackStackEntry>.asdBackEnter(): EnterTransition =
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(ASD_SCREEN_TRANSITION_MS),
    )

/** Matches ASD113's back animation: the current screen exits to the right. */
fun AnimatedContentTransitionScope<NavBackStackEntry>.asdBackExit(): ExitTransition =
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(ASD_SCREEN_TRANSITION_MS),
    )
