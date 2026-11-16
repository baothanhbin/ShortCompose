package com.baothanhbin.shortcompose.feature.offline.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.baothanhbin.shortcompose.feature.offline.OfflineScreen

const val OFFLINE_ROUTE = "offline"

fun NavGraphBuilder.offlineScreen() {
    composable(OFFLINE_ROUTE) { OfflineScreen() }
}
