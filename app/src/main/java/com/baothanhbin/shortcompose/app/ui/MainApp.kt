package com.baothanhbin.shortcompose.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import com.baothanhbin.shortcompose.R
import com.baothanhbin.shortcompose.app.MainViewModel
import com.baothanhbin.shortcompose.app.navigation.MainNavHost
import com.baothanhbin.shortcompose.app.navigation.TopLevelDestination
import com.baothanhbin.shortcompose.app.navigation.rememberAppState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(
    viewModel: MainViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val appState = rememberAppState()
    val currentDestination = appState.currentDestination()
    val labelRes = TopLevelDestination.entries
        .firstOrNull { destination -> currentDestination?.hierarchy?.any { it.route == destination.route } == true }
        ?.labelId
        ?: R.string.app_name

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(labelRes))
                },
            )
        },
        bottomBar = {
            if (TopLevelDestination.entries.size > 1) {
                NavigationBar {
                    TopLevelDestination.entries.forEach { destination ->
                        val selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = { appState.navigateToTopLevelDestination(destination) },
                            icon = {
                                Icon(
                                    painter = painterResource(
                                        id = if (selected) destination.selectedIconId else destination.unselectedIconId,
                                    ),
                                    contentDescription = stringResource(destination.labelId),
                                )
                            },
                            label = {
                                Text(text = stringResource(destination.labelId))
                            },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        if (uiState.isReady) {
            MainNavHost(
                navController = appState.navController,
                modifier = Modifier.padding(innerPadding),
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
