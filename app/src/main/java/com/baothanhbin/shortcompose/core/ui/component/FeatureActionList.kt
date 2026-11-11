package com.baothanhbin.shortcompose.core.ui.component

import androidx.compose.runtime.Composable
import com.baothanhbin.shortcompose.core.model.FeatureDestination

@Composable
fun FeatureActionList(
    destinations: List<FeatureDestination>,
    onNavigate: (String) -> Unit,
    onBackClick: (() -> Unit)?,
) {
}
