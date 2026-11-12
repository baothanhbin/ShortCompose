package com.baothanhbin.shortcompose.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import com.baothanhbin.shortcompose.app.MainViewModel
import com.baothanhbin.shortcompose.app.navigation.MainNavHost
import com.baothanhbin.shortcompose.app.navigation.TopLevelDestination
import com.baothanhbin.shortcompose.app.navigation.rememberAppState

@Composable
fun MainApp(
    viewModel: MainViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val appState = rememberAppState()
    val currentDestination = appState.currentDestination()
    val contentAlpha = remember { Animatable(1f) }
    // Mirrors ASD113's MainActivity: swap tab immediately, then fade the page from .96 to 1.
    LaunchedEffect(currentDestination?.route) {
        contentAlpha.snapTo(0.96f)
        contentAlpha.animateTo(1f, animationSpec = tween(durationMillis = 120))
    }

    Scaffold(
        // Screens render underneath the floating glass nav, just like the XML ViewPager.
        // Without this, Scaffold's default light surface is visible through the glass.
        containerColor = Color(0xFF130A0B),
        contentColor = Color.White,
        bottomBar = {
            CinemixBottomBar(
                selectedDestination = TopLevelDestination.entries.firstOrNull { destination ->
                    currentDestination?.hierarchy?.any { it.route == destination.route } == true
                } ?: TopLevelDestination.CINEMIX,
                onDestinationClick = appState::navigateToTopLevelDestination,
            )
        },
    ) { _ ->
        if (uiState.isReady) {
            MainNavHost(
                navController = appState.navController,
                modifier = Modifier
                    .graphicsLayer { alpha = contentAlpha.value },
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
