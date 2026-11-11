package com.baothanhbin.shortcompose.app.ui

import androidx.compose.runtime.Composable
import com.baothanhbin.shortcompose.core.theme.SingleModuleStarterTheme

@Composable
fun AppUiProviders(
    content: @Composable () -> Unit,
) {
    SingleModuleStarterTheme {
        content()
    }
}
