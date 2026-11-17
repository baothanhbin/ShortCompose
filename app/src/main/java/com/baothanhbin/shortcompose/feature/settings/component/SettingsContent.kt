package com.baothanhbin.shortcompose.feature.settings.component

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.widget.RatingBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.annotation.DrawableRes
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.tooling.preview.Preview
import com.baothanhbin.shortcompose.R
import com.baothanhbin.shortcompose.core.utils.drawableBackground
import com.baothanhbin.shortcompose.core.utils.Tap
import com.baothanhbin.shortcompose.core.ui.preview.PreviewTheme

@Composable
fun SettingsContent(
    title: String,
    onLanguageClick: () -> Unit,
    onAboutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var showRating by remember { mutableStateOf(false) }
    Column(modifier.fillMaxSize().paint(painterResource(R.drawable.bg_main), contentScale = ContentScale.Crop).padding(top = 16.dp)) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp).height(64.dp)
            .paint(painterResource(R.drawable.bg_top), contentScale = ContentScale.FillBounds).padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(title, color = Color.White, style = MaterialTheme.typography.titleLarge)
        }
        Column(Modifier.fillMaxWidth().padding(12.dp, 16.dp, 12.dp, 112.dp).drawableBackground(R.drawable.bg_downloaded_storage_card).padding(horizontal = 14.dp, vertical = 10.dp)) {
            SettingRow(R.drawable.ic_language, stringResource(R.string.action_language), onLanguageClick)
            SettingRow(R.drawable.ic_rate_us, stringResource(R.string.action_rate_us)) { showRating = true }
            SettingRow(R.drawable.ic_share, stringResource(R.string.action_share)) {
                context.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"; putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=${context.packageName}")
                }, context.getString(R.string.settings_share_chooser)))
            }
            SettingRow(R.drawable.ic_about, stringResource(R.string.action_about), onAboutClick)
        }
    }
    if (showRating) RatingDialog(
        onDismiss = { showRating = false },
        onSubmit = { rating ->
            showRating = false
            if (rating >= 4) runCatching {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${context.packageName}")))
            }.recoverCatching {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")))
            }
        },
    )
}

@Composable
private fun RatingDialog(onDismiss: () -> Unit, onSubmit: (Int) -> Unit) {
    var rating by remember { mutableStateOf(5) }
    Dialog(onDismissRequest = onDismiss) {
        Column(Modifier.fillMaxWidth().drawableBackground(R.drawable.bg_rating_dialog).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painterResource(listOf(R.drawable.rating_0, R.drawable.rating_1, R.drawable.rating_2, R.drawable.rating_3, R.drawable.rating_4, R.drawable.rating_5)[rating]), null, Modifier.size(72.dp))
            Text(stringResource(R.string.rating_title), color = Color.White, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 12.dp))
            Text(stringResource(R.string.rating_message), color = Color.White, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 8.dp))
            AndroidView(
                factory = { context ->
                    RatingBar(context).apply {
                        numStars = 5
                        stepSize = 1f
                        progressTintList = ColorStateList.valueOf(android.graphics.Color.rgb(255, 247, 50))
                        secondaryProgressTintList = progressTintList
                        progressBackgroundTintList = ColorStateList.valueOf(android.graphics.Color.argb(77, 255, 247, 50))
                        setOnRatingBarChangeListener { _, value, fromUser -> if (fromUser) rating = value.toInt() }
                    }
                },
                update = { if (it.rating.toInt() != rating) it.rating = rating.toFloat() },
                modifier = Modifier.padding(top = 12.dp).height(48.dp),
            )
            RatingButton(if (rating in 1..3) stringResource(R.string.action_thank_you) else stringResource(R.string.action_rate_us), R.drawable.bg_age_button_primary) { if (rating > 0) onSubmit(rating) }
            RatingButton(stringResource(R.string.action_not_now), R.drawable.bg_age_button_secondary, onDismiss)
        }
    }
}

@Composable
private fun RatingButton(text: String, @DrawableRes background: Int, onClick: () -> Unit) = Box(
    Modifier.padding(top = 10.dp).width(140.dp).height(42.dp).drawableBackground(background).Tap(onClick = onClick), contentAlignment = Alignment.Center,
) { Text(text, color = Color.White) }

@Composable
private fun SettingRow(@DrawableRes icon: Int, label: String, onClick: () -> Unit) = Row(
    Modifier.fillMaxWidth().height(52.dp).Tap(onClick = onClick), verticalAlignment = Alignment.CenterVertically,
) {
    Image(painterResource(icon), label, Modifier.size(26.dp))
    Text(label, color = Color.White, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f).padding(start = 14.dp))
    Image(painterResource(R.drawable.ic_chevron), null, Modifier.size(24.dp))
}

@Preview(name = "Settings", showBackground = true, showSystemUi = true)
@Composable
private fun SettingsContentPreview() = PreviewTheme {
    SettingsContent(title = stringResource(R.string.settings_title), onLanguageClick = {}, onAboutClick = {})
}

@Preview(name = "Rating dialog", showBackground = true)
@Composable
private fun RatingDialogPreview() = PreviewTheme {
    RatingDialog(onDismiss = {}, onSubmit = {})
}
