package com.baothanhbin.shortcompose.feature.onboarding.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.baothanhbin.shortcompose.R
import com.baothanhbin.shortcompose.core.ui.drawableBackground
import com.baothanhbin.shortcompose.core.ui.asdTap
import com.baothanhbin.shortcompose.core.ui.preview.PreviewTheme
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

private val Accent = Color(0xFFEA7B71)

private data class LanguageItem(val code: String, @StringRes val nameRes: Int, @DrawableRes val icon: Int)

private val languages = listOf(
    LanguageItem("en", R.string.language_english, R.drawable.ic_lang_en),
    LanguageItem("es", R.string.language_spanish, R.drawable.ic_lang_es),
    LanguageItem("fr", R.string.language_french, R.drawable.ic_lang_fr),
    LanguageItem("de", R.string.language_german, R.drawable.ic_lang_de),
    LanguageItem("pt", R.string.language_portuguese, R.drawable.ic_lang_pt),
    LanguageItem("hi", R.string.language_hindi, R.drawable.ic_lang_hi),
    LanguageItem("ja", R.string.language_japanese, R.drawable.ic_lang_ja),
    LanguageItem("ko", R.string.language_korean, R.drawable.ic_lang_ko),
    LanguageItem("ru", R.string.language_russian, R.drawable.ic_lang_ru),
)

@Composable
fun SplashContent() = Box(
    Modifier.fillMaxSize().paint(painterResource(R.drawable.bg_main), contentScale = ContentScale.Crop),
    contentAlignment = Alignment.Center,
) {
    Image(
        painter = painterResource(R.drawable.logo_app),
        contentDescription = stringResource(R.string.app_name),
        modifier = Modifier.size(128.dp),
    )
}

@Composable
fun LanguageSelectionContent(
    selected: String, onSelect: (String) -> Unit, onContinue: () -> Unit
) = Column(
    Modifier
        .fillMaxSize()
        .paint(painterResource(R.drawable.bg_main), contentScale = ContentScale.Crop),
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .height(78.dp)
            .paint(painterResource(R.drawable.bg_top), contentScale = ContentScale.FillBounds)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                stringResource(R.string.language_choose_title), color = Color.White, style = MaterialTheme.typography.titleMedium
            )
            Text(
                stringResource(R.string.language_choose_message),
                color = Color.White.copy(.6f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Image(
            painterResource(R.drawable.iv_check),
            stringResource(R.string.action_continue),
            Modifier
                .size(48.dp)
                .asdTap(onClick = onContinue)
        )
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        languages.forEach { item ->
            val itemName = stringResource(item.nameRes)
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(bottom = 6.dp)
                    .drawableBackground(if (selected == item.code) R.drawable.bg_language_card_selected else R.drawable.bg_language_card_normal)
                    .asdTap { onSelect(item.code) }
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painterResource(item.icon),
                    itemName,
                    Modifier.size(48.dp),
                    contentScale = ContentScale.FillBounds
                )
                Text(
                    itemName,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }
    }
}

@Composable
fun IntroPagerContent(onFinished: () -> Unit) {
    val pagerState = rememberPagerState { 3 }
    val scope = rememberCoroutineScope()
    Box(Modifier.fillMaxSize().background(Color.Black)) {
        HorizontalPager(
            state = pagerState,
            pageSpacing = 100.dp,
            beyondViewportPageCount = 2,
            modifier = Modifier.fillMaxSize().padding(bottom = 56.dp),
        ) { page ->
            val position = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction)
                .absoluteValue.coerceIn(0f, 1f)
            IntroPageContent(
                page = page,
                modifier = Modifier.graphicsLayer {
                    scaleY = 0.8f + (1f - position) * 0.2f
                    alpha = 1f - 0.7f * position
                },
            )
        }
        Row(
            Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(56.dp)
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(3) { index ->
                    Image(
                        painterResource(if (index == pagerState.currentPage) R.drawable.ic_intro_s else R.drawable.ic_intro_sn),
                        contentDescription = null,
                    )
                }
            }
            Text(
                stringResource(if (pagerState.currentPage == 2) R.string.action_continue else R.string.action_next),
                color = Accent,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.asdTap {
                    if (pagerState.currentPage == 2) onFinished()
                    else scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                }.padding(12.dp),
            )
        }
    }
}

@Composable
private fun IntroPageContent(page: Int, modifier: Modifier = Modifier) {
    val titles = listOf(
        stringResource(R.string.intro_title_one),
        stringResource(R.string.intro_title_two),
        stringResource(R.string.intro_title_three),
    )
    val messages = listOf(
        stringResource(R.string.intro_message_one),
        stringResource(R.string.intro_message_two),
        stringResource(R.string.intro_message_three),
    )
    Box(modifier.fillMaxSize().background(Color.Black)) {
        AsyncImage(
            model = "file:///android_asset/images/intro/bg_intro_0${page + 1}.png",
            contentDescription = titles[page],
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize(.8f)
                .align(Alignment.TopCenter),
        )
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                titles[page],
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Text(
                messages[page],
                color = Color(0xFFD7D9D1),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun PermissionContent(onRequest: () -> Unit, onSkip: () -> Unit) = Column(
    Modifier
        .fillMaxSize()
        .paint(painterResource(R.drawable.bg_main), contentScale = ContentScale.Crop)
        .padding(horizontal = 18.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    Text(
        stringResource(R.string.permission_title),
        color = Color.White,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(top = 48.dp)
    )
    Image(
        painterResource(R.drawable.iv_permission), null, Modifier
            .padding(top = 18.dp)
            .size(230.dp)
    )
    Text(
        stringResource(R.string.permission_message),
        color = Color.White.copy(.8f),
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
    )
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 14.dp)
            .height(55.dp)
            .drawableBackground(R.drawable.bg_permission_row)
            .asdTap(onClick = onRequest)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            stringResource(R.string.permission_notification),
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Image(
            painterResource(R.drawable.iv_sw_off), null, Modifier
                .width(53.dp)
                .height(30.dp)
        )
    }
    Spacer(Modifier.weight(1f))
    Text(
        stringResource(R.string.permission_agree_continue),
        color = Accent,
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier
            .asdTap(onClick = onRequest)
            .padding(12.dp)
    )
    Text(
        stringResource(R.string.action_skip_continue),
        color = Color.White.copy(.65f),
        modifier = Modifier
            .asdTap(onClick = onSkip)
            .padding(bottom = 12.dp)
    )
}

@Composable
fun AgeConfirmContent(onAgree: () -> Unit, onDeny: () -> Unit) = AgeScreen(
    title = stringResource(R.string.age_confirmation_title),
    message = stringResource(R.string.age_confirmation_message),
    art = R.drawable.iv_age_restriction,
    primary = stringResource(R.string.age_confirmation_yes),
    secondary = stringResource(R.string.action_no),
    onPrimary = onAgree,
    onSecondary = onDeny,
)

@Composable
fun AgeRestrictionContent(onRetry: () -> Unit) = AgeScreen(
    title = stringResource(R.string.age_restriction_title),
    message = stringResource(R.string.age_restriction_message),
    art = R.drawable.iv_age_restriction,
    primary = stringResource(R.string.action_try_again),
    secondary = null,
    onPrimary = onRetry,
)

@Composable
private fun AgeScreen(
    title: String, message: String, @DrawableRes art: Int, primary: String, secondary: String?,
    onPrimary: () -> Unit, onSecondary: () -> Unit = {},
) = Column(
    Modifier
        .fillMaxSize()
        .paint(painterResource(R.drawable.bg_main), contentScale = ContentScale.Crop),
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .height(64.dp)
            .paint(painterResource(R.drawable.bg_top), contentScale = ContentScale.FillBounds),
        contentAlignment = Alignment.Center
    ) {
        Text(title, color = Color.White, style = MaterialTheme.typography.titleLarge)
    }
    Spacer(Modifier.weight(1f)); Image(
    painterResource(art), null, Modifier.size(231.dp), contentScale = ContentScale.Fit
)
    Text(
        message,
        color = Color.White.copy(.8f),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(24.dp)
    )
    ActionButton(primary, R.drawable.bg_age_button_primary, onPrimary)
    secondary?.let { ActionButton(it, R.drawable.bg_age_button_secondary, onSecondary) }
    Spacer(Modifier.weight(1f))
}

@Composable
private fun ActionButton(text: String, @DrawableRes background: Int, onClick: () -> Unit) = Box(
    Modifier
        .width(220.dp)
        .height(48.dp)
        .padding(vertical = 4.dp)
        .drawableBackground(background)
        .asdTap(onClick = onClick),
    contentAlignment = Alignment.Center,
) { Text(text, color = Color.White, style = MaterialTheme.typography.labelLarge) }

@Preview(name = "Onboarding - Language", showBackground = true, showSystemUi = true)
@Composable
private fun LanguageSelectionPreview() = PreviewTheme {
    LanguageSelectionContent(selected = "en", onSelect = {}, onContinue = {})
}

@Preview(name = "Splash", showBackground = true, showSystemUi = true)
@Composable
private fun SplashPreview() = PreviewTheme { SplashContent() }

@Preview(name = "Onboarding - Intro 1", showBackground = true)
@Composable
private fun IntroOnePreview() = PreviewTheme { IntroPageContent(page = 0) }

@Preview(name = "Onboarding - Intro 2", showBackground = true)
@Composable
private fun IntroTwoPreview() = PreviewTheme { IntroPageContent(page = 1) }

@Preview(name = "Onboarding - Intro 3", showBackground = true)
@Composable
private fun IntroThreePreview() = PreviewTheme { IntroPageContent(page = 2) }

@Preview(name = "Onboarding - Permission", showBackground = true)
@Composable
private fun PermissionPreview() = PreviewTheme { PermissionContent(onRequest = {}, onSkip = {}) }

@Preview(name = "Onboarding - Age confirmation", showBackground = true)
@Composable
private fun AgeConfirmPreview() = PreviewTheme { AgeConfirmContent(onAgree = {}, onDeny = {}) }

@Preview(name = "Onboarding - Age restriction", showBackground = true)
@Composable
private fun AgeRestrictionPreview() = PreviewTheme { AgeRestrictionContent(onRetry = {}) }
