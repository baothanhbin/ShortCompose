package com.baothanhbin.shortcompose.feature.offline.component

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.baothanhbin.shortcompose.R
import com.baothanhbin.shortcompose.core.ui.drawableBackground
import com.baothanhbin.shortcompose.core.ui.asdTap
import com.baothanhbin.shortcompose.core.ui.preview.PreviewTheme

@Composable
fun OfflineContent() {
    val context = LocalContext.current
    Column(
        Modifier
            .fillMaxSize()
            .paint(painterResource(R.drawable.bg_main), contentScale = ContentScale.Crop)
            .padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(61.dp)
                .paint(painterResource(R.drawable.bg_top), contentScale = ContentScale.FillBounds),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.offline_screen_title), color = Color.White, style = MaterialTheme.typography.titleLarge)
        }
        Spacer(Modifier.weight(1f)); Image(
        painterResource(R.drawable.iv_no_internet), null, Modifier.size(231.dp)
    )
        Text(
            stringResource(R.string.offline_title),
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            stringResource(R.string.offline_message),
            color = Color.White.copy(.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
        Spacer(Modifier.height(24.dp))
        Box(
            Modifier
                .width(148.dp)
                .height(48.dp)
                .drawableBackground(R.drawable.bg_age_button_primary)
                .asdTap {
                    runCatching { context.startActivity(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)) }.recoverCatching {
                        context.startActivity(
                            Intent(Settings.ACTION_WIFI_SETTINGS)
                        )
                    }
                }, contentAlignment = Alignment.Center
        ) { Text(stringResource(R.string.action_try_again), color = Color.White, style = MaterialTheme.typography.labelLarge) }
        Spacer(Modifier.weight(1f))
    }
}

@Preview(name = "Offline", showBackground = true, showSystemUi = true)
@Composable
private fun OfflineContentPreview() = PreviewTheme { OfflineContent() }
