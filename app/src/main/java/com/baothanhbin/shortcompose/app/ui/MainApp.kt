package com.baothanhbin.shortcompose.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.background
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.baothanhbin.shortcompose.R
import com.baothanhbin.shortcompose.core.utils.drawableBackground
import com.baothanhbin.shortcompose.core.utils.Tap
import androidx.compose.foundation.layout.padding
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baothanhbin.shortcompose.app.MainViewModel
import com.baothanhbin.shortcompose.app.navigation.MAIN_ROUTE
import com.baothanhbin.shortcompose.app.navigation.MainNavHost
import com.baothanhbin.shortcompose.app.navigation.TopLevelDestination
import com.baothanhbin.shortcompose.app.navigation.rememberAppState
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.window.Dialog
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Composable
fun MainApp(
    viewModel: MainViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val appState = rememberAppState()
    val currentDestination = appState.currentDestination()
    val isTopLevel = currentDestination?.route == MAIN_ROUTE
    var selectedDestination by rememberSaveable {
        mutableStateOf(TopLevelDestination.CINEMIX)
    }
    var showExitDialog by remember { mutableStateOf(false) }
    BackHandler(enabled = isTopLevel) { showExitDialog = true }
    if (showExitDialog) {
        val activity = LocalActivity.current
        Dialog(onDismissRequest = { showExitDialog = false }) {
            Column(
                Modifier.fillMaxWidth()
                    .drawableBackground(R.drawable.bg_exit_dialog)
                    .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    Modifier.fillMaxWidth().height(110.dp)
                        .paint(painterResource(R.drawable.bg_top_exit_app), contentScale = ContentScale.FillBounds),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(stringResource(R.string.exit_title), color = Color.White, style = MaterialTheme.typography.headlineSmall)
                    Text(
                        stringResource(R.string.exit_message),
                        color = Color.White.copy(alpha = .8f),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
                Row(
                    Modifier.fillMaxWidth().padding(top = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    ExitDialogButton(
                        label = stringResource(R.string.action_no),
                        background = R.drawable.bg_age_button_secondary,
                        modifier = Modifier.weight(1f),
                    ) { showExitDialog = false }
                    ExitDialogButton(
                        label = stringResource(R.string.action_yes),
                        background = R.drawable.bg_age_button_primary,
                        modifier = Modifier.weight(1f),
                    ) {
                        showExitDialog = false
                        activity?.finishAffinity()
                    }
                }
            }
        }
    }
    // Keep the NavHost at a constant size. Adding/removing Scaffold's bottom bar
    // during pop changes the host bounds and interrupts its back transition.
    Box(Modifier.fillMaxSize().background(Color(0xFF130A0B))) {
        MainNavHost(
            navController = appState.navController,
            selectedDestination = selectedDestination,
            isAppReady = uiState.isReady,
            onOnboardingComplete = viewModel::completeOnboarding,
            modifier = Modifier.fillMaxSize(),
        )
        AnimatedVisibility(
            visible = isTopLevel,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = fadeIn(tween(180)) + slideInVertically(tween(220)) { it / 2 },
            exit = fadeOut(tween(120)) + slideOutVertically(tween(180)) { it / 2 },
        ) {
            CinemixBottomBar(
                selectedDestination = selectedDestination,
                onDestinationClick = { selectedDestination = it },
            )
        }
    }
}

@Composable
private fun ExitDialogButton(
    label: String,
    background: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) = Box(
    modifier.height(40.dp)
        .drawableBackground(background)
        .Tap(onClick = onClick),
    contentAlignment = Alignment.Center,
) {
    Text(label, color = Color.White, style = MaterialTheme.typography.labelLarge)
}

