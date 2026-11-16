package com.baothanhbin.shortcompose.feature.settings.component

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import java.util.Locale
import com.baothanhbin.shortcompose.R
import com.baothanhbin.shortcompose.core.ui.drawableBackground
import com.baothanhbin.shortcompose.core.ui.asdTap
import androidx.compose.ui.tooling.preview.Preview
import com.baothanhbin.shortcompose.core.ui.preview.PreviewTheme

private const val POLICY_URL = "https://sites.google.com/view/privatepolicyasd105shortdrama?usp=sharing"

@Composable
fun LanguagePreferencesContent(onBack: () -> Unit, onSaved: () -> Unit) {
    val context = LocalContext.current
    val languages = listOf(
        Triple("en", stringResource(R.string.language_english), R.drawable.ic_lang_en),
        Triple("es", stringResource(R.string.language_spanish), R.drawable.ic_lang_es),
        Triple("fr", stringResource(R.string.language_french), R.drawable.ic_lang_fr),
        Triple("de", stringResource(R.string.language_german), R.drawable.ic_lang_de),
        Triple("pt", stringResource(R.string.language_portuguese), R.drawable.ic_lang_pt),
        Triple("hi", stringResource(R.string.language_hindi), R.drawable.ic_lang_hi),
        Triple("ja", stringResource(R.string.language_japanese), R.drawable.ic_lang_ja),
        Triple("ko", stringResource(R.string.language_korean), R.drawable.ic_lang_ko),
        Triple("ru", stringResource(R.string.language_russian), R.drawable.ic_lang_ru),
    )
    var selected by remember { mutableStateOf(context.getSharedPreferences("locale", Context.MODE_PRIVATE).getString("code", "en") ?: "en") }
    Column(Modifier.fillMaxSize().paint(painterResource(R.drawable.bg_main), contentScale = ContentScale.Crop)) {
        ScreenTopBar(stringResource(R.string.action_language), onBack, action = stringResource(R.string.action_continue)) {
            context.getSharedPreferences("locale", Context.MODE_PRIVATE).edit().putString("code", selected).apply()
            applyLocale(context, selected)
            onSaved()
        }
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            languages.forEach { (code, name, icon) ->
                Row(Modifier.fillMaxWidth().height(60.dp).padding(vertical = 3.dp)
                    .drawableBackground(if (selected == code) R.drawable.bg_language_card_selected else R.drawable.bg_language_card_normal)
                    .asdTap { selected = code }.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Image(painterResource(icon), name, Modifier.size(40.dp))
                    Text(name, color = Color.White, modifier = Modifier.weight(1f).padding(start = 12.dp))
                }
            }
        }
    }
}

@Composable
fun AboutContent(onBack: () -> Unit, onPolicy: () -> Unit) {
    Column(Modifier.fillMaxSize().paint(painterResource(R.drawable.bg_main), contentScale = ContentScale.Crop)) {
        ScreenTopBar(stringResource(R.string.action_about), onBack)
        Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(72.dp))
            Image(painterResource(R.drawable.logo_app), stringResource(R.string.app_name), Modifier.size(124.dp))
            Text(stringResource(R.string.about_subtitle), color = Color.White, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
            Text(stringResource(R.string.about_version), color = Color.White.copy(.65f), modifier = Modifier.padding(top = 8.dp))
            Spacer(Modifier.weight(1f))
            Text(stringResource(R.string.privacy_policy_title), color = Color(0xFFEA7B71), modifier = Modifier.asdTap(onClick = onPolicy).padding(16.dp))
        }
    }
}

@Composable
fun PolicyContent(onBack: () -> Unit) {
    val inspectionMode = LocalInspectionMode.current
    Column(Modifier.fillMaxSize().paint(painterResource(R.drawable.bg_main), contentScale = ContentScale.Crop)) {
        ScreenTopBar(stringResource(R.string.privacy_policy_title), onBack)
        if (inspectionMode) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.privacy_policy_preview), color = Color.White.copy(alpha = .7f))
            }
        } else {
            AndroidView(
                factory = { context -> WebView(context).apply { webViewClient = WebViewClient(); settings.javaScriptEnabled = true; loadUrl(POLICY_URL) } },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
fun ScreenTopBar(title: String, onBack: () -> Unit, action: String? = null, onAction: () -> Unit = {}) = Row(
    Modifier.fillMaxWidth().padding(top = 16.dp, start = 12.dp, end = 12.dp).height(64.dp)
        .paint(painterResource(R.drawable.bg_top), contentScale = ContentScale.FillBounds).padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically,
) {
    Image(painterResource(R.drawable.iv_back), stringResource(R.string.action_back), Modifier.size(48.dp).asdTap(onClick = onBack))
    Text(title, color = Color.White, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f).padding(start = 8.dp))
    action?.let { Image(painterResource(R.drawable.iv_check), it, Modifier.size(48.dp).asdTap(onClick = onAction)) }
}

private fun applyLocale(context: Context, language: String) {
    val locale = Locale.forLanguageTag(language)
    Locale.setDefault(locale)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.getSystemService(android.app.LocaleManager::class.java).applicationLocales = LocaleList(locale)
    } else {
        @Suppress("DEPRECATION")
        context.resources.updateConfiguration(Configuration(context.resources.configuration).apply { setLocale(locale) }, context.resources.displayMetrics)
    }
}

@Preview(name = "Language preferences", showBackground = true, showSystemUi = true)
@Composable
private fun LanguagePreferencesPreview() = PreviewTheme {
    LanguagePreferencesContent(onBack = {}, onSaved = {})
}

@Preview(name = "About", showBackground = true)
@Composable
private fun AboutPreview() = PreviewTheme {
    AboutContent(onBack = {}, onPolicy = {})
}

@Preview(name = "Privacy policy", showBackground = true)
@Composable
private fun PolicyPreview() = PreviewTheme {
    PolicyContent(onBack = {})
}
