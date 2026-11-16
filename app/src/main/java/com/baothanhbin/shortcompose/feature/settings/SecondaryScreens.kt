package com.baothanhbin.shortcompose.feature.settings

import androidx.compose.runtime.Composable
import com.baothanhbin.shortcompose.feature.settings.component.AboutContent
import com.baothanhbin.shortcompose.feature.settings.component.LanguagePreferencesContent
import com.baothanhbin.shortcompose.feature.settings.component.PolicyContent

@Composable
fun LanguagePreferencesScreen(onBack: () -> Unit, onSaved: () -> Unit) =
    LanguagePreferencesContent(onBack = onBack, onSaved = onSaved)

@Composable
fun AboutScreen(onBack: () -> Unit, onPolicy: () -> Unit) =
    AboutContent(onBack = onBack, onPolicy = onPolicy)

@Composable
fun PolicyScreen(onBack: () -> Unit) = PolicyContent(onBack = onBack)
