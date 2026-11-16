package com.baothanhbin.shortcompose.feature.onboarding

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.baothanhbin.shortcompose.R
import com.baothanhbin.shortcompose.feature.onboarding.component.AgeConfirmContent
import com.baothanhbin.shortcompose.feature.onboarding.component.AgeRestrictionContent
import com.baothanhbin.shortcompose.feature.onboarding.component.IntroPagerContent
import com.baothanhbin.shortcompose.feature.onboarding.component.LanguageSelectionContent
import com.baothanhbin.shortcompose.feature.onboarding.component.PermissionContent
import com.baothanhbin.shortcompose.feature.onboarding.component.SplashContent
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(isAppReady: Boolean, onFinished: () -> Unit) {
    var minimumDurationFinished by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(1_500)
        minimumDurationFinished = true
    }
    LaunchedEffect(isAppReady, minimumDurationFinished) {
        if (isAppReady && minimumDurationFinished) onFinished()
    }
    SplashContent()
}

@Composable
fun LanguageScreen(onContinue: () -> Unit) {
    val context = LocalContext.current
    var selectedCode by remember { mutableStateOf("en") }
    LanguageSelectionContent(
        selected = selectedCode,
        onSelect = { selectedCode = it },
        onContinue = {
            context.getSharedPreferences("locale", Context.MODE_PRIVATE).edit()
                .putString("code", selectedCode).apply()
            onContinue()
        },
    )
}

@Composable
fun IntroScreen(onFinished: () -> Unit) = IntroPagerContent(onFinished = onFinished)

@Composable
fun PermissionScreen(onContinue: () -> Unit) {
    val context = LocalContext.current
    var permissionDenied by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) onContinue() else permissionDenied = true
    }
    val requestPermission = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            onContinue()
        }
    }
    PermissionContent(onRequest = requestPermission, onSkip = onContinue)
    if (permissionDenied) {
        AlertDialog(
            onDismissRequest = { permissionDenied = false; onContinue() },
            title = { Text(stringResource(R.string.permission_notification)) },
            text = { Text(stringResource(R.string.permission_denied_message)) },
            confirmButton = {
                TextButton(onClick = {
                    permissionDenied = false
                    context.startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:${context.packageName}"),
                        ),
                    )
                    onContinue()
                }) { Text(stringResource(R.string.action_open_settings)) }
            },
            dismissButton = {
                TextButton(onClick = { permissionDenied = false; onContinue() }) {
                    Text(stringResource(R.string.action_continue))
                }
            },
        )
    }
}

@Composable
fun AgeConfirmScreen(onComplete: () -> Unit, onRestricted: () -> Unit) =
    AgeConfirmContent(onAgree = onComplete, onDeny = onRestricted)

@Composable
fun AgeRestrictionScreen(onRetry: () -> Unit) = AgeRestrictionContent(onRetry = onRetry)

@Composable
fun NotificationPermissionScreen(onContinue: () -> Unit) = PermissionScreen(onContinue = onContinue)
