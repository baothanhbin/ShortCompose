package com.baothanhbin.shortcompose.feature.cinemix.component

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.baothanhbin.shortcompose.R
import com.baothanhbin.shortcompose.core.model.Movie
import com.baothanhbin.shortcompose.core.utils.drawableBackground
import com.baothanhbin.shortcompose.core.utils.Tap
import com.baothanhbin.shortcompose.core.ui.component.MovieCard
import com.baothanhbin.shortcompose.core.ui.preview.PreviewTheme
import com.baothanhbin.shortcompose.core.ui.preview.previewHomeContent
import com.baothanhbin.shortcompose.feature.cinemix.CinemixUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

private val Background = Color(0xFF130A0B)
private val Accent = Color(0xFFEA7B71)
private const val BANNER_WIDTH_RATIO = 0.65f // modifer heigh banner
private const val BANNER_ASPECT_RATIO = 0.75f
private const val BANNER_MIN_SCALE = 0.86f
private const val MAX_BANNER_COUNT = 7
private const val BANNER_AUTO_SCROLL_DELAY_MS = 8_000L
private val InfiniteBannerPageCount: () -> Int = { Int.MAX_VALUE }
private val SingleBannerPageCount: () -> Int = { 1 }
private val BannerShape = RoundedCornerShape(24.dp)
private val BannerBackgroundBrush = Brush.linearGradient(listOf(Color(0xFF6A3038), Background))
private val BannerOverlayBrush = Brush.verticalGradient(
    listOf(Color.Transparent, Color(0xD0000000))
)

@Composable
fun CinemixContent(
    state: CinemixUiState,
    onRetry: () -> Unit,
    onMovieClick: (Long) -> Unit,
    onFavoriteClick: (Movie) -> Unit,
    onSearchClick: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onSeeAllClick: (String) -> Unit,
) {
    Box(
        Modifier
            .fillMaxSize()
            // Do not draw the red/white skeleton during loading. It causes a
            // noticeable flash when returning from a full-screen player.
            .background(Background)
    ) {
        when {
            // Keep already loaded content visible while a refresh happens in
            // the background. Replacing it with a skeleton makes navigation
            // look like a reload and adds unnecessary composition work.
            state.content != null -> HomeContent(
                content = state.content,
                onMovieClick = onMovieClick,
                onFavoriteClick = onFavoriteClick,
                onSearchClick = onSearchClick,
                onCategoryClick = onCategoryClick,
                onSeeAllClick = onSeeAllClick,
            )

            state.isLoading -> CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(32.dp),
                color = Color.White.copy(alpha = 0.7f),
                trackColor = Color.White.copy(alpha = 0.14f),
            )
            state.error != null || state.errorRes != null -> Column(
                Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(state.error ?: stringResource(state.errorRes!!), color = Color.White)
                Text(
                    stringResource(R.string.action_try_again),
                    color = Accent,
                    modifier = Modifier
                        .padding(16.dp)
                        .Tap(onClick = onRetry)
                )
            }

        }
    }
}

@Composable
private fun HomeContent(
    content: com.baothanhbin.shortcompose.core.model.HomeContent,
    onMovieClick: (Long) -> Unit,
    onFavoriteClick: (Movie) -> Unit,
    onSearchClick: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onSeeAllClick: (String) -> Unit,
) = Column(
    modifier = Modifier
        .fillMaxSize()
        .paint(painterResource(R.drawable.bg_main), contentScale = ContentScale.Crop)
) {
    TopBar(onSearchClick)
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(top = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (content.banners.isNotEmpty()) {
            BannerPager(content.banners, onMovieClick, onFavoriteClick)
        }
        SectionHeader(stringResource(R.string.cinemix_feel_the_vibes))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(content.categories, key = { it.queryName }) { category ->
                Text(
                    category.name,
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .Tap { onCategoryClick(category.queryName) }
                        .drawableBackground(R.drawable.bg_cinemix_chip)
                        .padding(horizontal = 16.dp, vertical = 11.dp))
            }
        }
        MovieSection(stringResource(R.string.cinemix_trending_now), content.trending, onMovieClick) {
            onSeeAllClick("trending")
        }
        MovieSection(stringResource(R.string.cinemix_new_releases), content.newRelease, onMovieClick) {
            onSeeAllClick("new")
        }
        MovieSection(stringResource(R.string.cinemix_spotlight), content.spotlight, onMovieClick) {
            onSeeAllClick("spotlight")
        }
        content.spotlight.firstOrNull()?.let { movie ->
            EditorChoice(movie) { onMovieClick(movie.id) }
        }
    }
}

@Composable
private fun TopBar(onSearchClick: () -> Unit) = Row(
    Modifier
        .fillMaxWidth()
        .padding(top = 35.dp, start = 16.dp, end = 16.dp)
        .height(64.dp)
        .paint(painterResource(R.drawable.bg_top), contentScale = ContentScale.FillBounds)
        .padding(horizontal = 8.dp),
    verticalAlignment = Alignment.CenterVertically,
) {
    Text(
        stringResource(R.string.app_name),
        color = Color.White,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.weight(1f)
    )
    Image(
        painterResource(R.drawable.iv_seach),
        stringResource(R.string.action_search),
        modifier = Modifier
            .size(48.dp)
            .Tap(onClick = onSearchClick)
    )
}

@Composable
private fun BannerPager(
    movies: List<Movie>,
    onMovieClick: (Long) -> Unit,
    onFavoriteClick: (Movie) -> Unit,
) {
    val banners = remember(movies) { movies.take(MAX_BANNER_COUNT) }
    val isInfinite = banners.size > 1
    val initialPage = remember(banners.size) {
        if (isInfinite) {
            val middle = Int.MAX_VALUE / 2
            middle - middle % banners.size
        } else {
            0
        }
    }
    // Top-level providers avoid a captured synthetic lambda that Android Studio Live Edit
    // cannot safely replace after the pager implementation changes.
    val pageCountProvider = if (isInfinite) InfiniteBannerPageCount else SingleBannerPageCount
    val state = rememberPagerState(
        initialPage = initialPage,
        pageCount = pageCountProvider,
    )
    // Restart the countdown only after a manual fling has fully settled. This avoids
    // competing with the user's gesture or starting the next slide mid-settle.
    LaunchedEffect(state, isInfinite) {
        if (!isInfinite) return@LaunchedEffect

        snapshotFlow { state.isScrollInProgress }.collectLatest { isScrolling ->
                if (!isScrolling) {
                    delay(BANNER_AUTO_SCROLL_DELAY_MS)
                    if (!state.isScrollInProgress) {
                        launch { state.animateScrollToPage(state.currentPage + 1) }
                    }
                }
            }
    }
    val density = LocalDensity.current
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val pageWidth = maxWidth * BANNER_WIDTH_RATIO
        val pageHeight = pageWidth / BANNER_ASPECT_RATIO
        val horizontalPadding = (maxWidth - pageWidth) / 2
        val pageWidthPx = with(density) { pageWidth.toPx() }

        HorizontalPager(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .height(pageHeight),
            pageSize = PageSize.Fixed(pageWidth),
            contentPadding = PaddingValues(horizontal = horizontalPadding),
            pageSpacing = 12.dp,
            beyondViewportPageCount = 1,
            key = { page -> page },
        ) { page ->
            val movie = banners[page % banners.size]
            val rating = remember(movie.rating) {
                if (movie.rating > 0) "%.1f".format(movie.rating) else "4.7"
            }
            val pageOffset =
                ((page - state.currentPage) - state.currentPageOffsetFraction).coerceIn(-1f, 1f)
            val distance = pageOffset.absoluteValue
            val pageScale = 1f - distance * (1f - BANNER_MIN_SCALE)
            val shrinkMarginPx = pageWidthPx * (1f - pageScale) / 2f

            Box(Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = pageScale
                    scaleY = pageScale
                    translationX = -pageOffset * shrinkMarginPx
                    transformOrigin = TransformOrigin.Center
                }
                .Tap { onMovieClick(movie.id) }
                .clip(BannerShape)
                .background(BannerBackgroundBrush)) {
                AsyncImage(
                    movie.posterUrl,
                    movie.title,
                    Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    Modifier
                        .matchParentSize()
                        .background(BannerOverlayBrush)
                )
                Text(
                    stringResource(R.string.cinemix_for_you),
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                        .height(24.dp)
                        .drawableBackground(R.drawable.bg_cinemix_badge)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                )
                Row(
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .height(24.dp)
                        .drawableBackground(R.drawable.bg_cinemix_rating),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        rating,
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                    Image(painterResource(R.drawable.ic_cinemix_star), null, Modifier.size(24.dp))
                }
                Row(
                    Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 10.dp, bottom = 10.dp, end = 66.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                        // Tap must wrap the background and padding; otherwise only the
                        // content after it (the icon/text) receives the visual scale.
                        .Tap { onMovieClick(movie.id) }
                        .drawableBackground(R.drawable.bg_age_button_primary)
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Image(painterResource(R.drawable.ic_cinemix_play), null, Modifier.size(24.dp))
                    Text(
                        stringResource(R.string.action_watch_now),
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Box(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 10.dp, bottom = 10.dp)
                        .size(48.dp)
                        .Tap { onFavoriteClick(movie) }
                        .drawableBackground(R.drawable.bg_cinemix_banner),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painterResource(if (movie.isFavorite) R.drawable.ic_cinemix_favorite_on else R.drawable.ic_cinemix_favorite_off),
                        stringResource(R.string.action_favorite),
                        Modifier.size(24.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun EditorChoice(movie: Movie, onClick: () -> Unit) = Box(
    Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .padding(bottom = 80.dp)
        .height(202.dp)
        .clip(RoundedCornerShape(20.dp))
        .background(Color.White.copy(.09f))
        .Tap(onClick = onClick),
) {
    AsyncImage(
        movie.posterUrl, movie.title, Modifier.matchParentSize(), contentScale = ContentScale.Crop
    )
    Box(
        Modifier
            .matchParentSize()
            .drawableBackground(R.drawable.bg_cinemix_editor_overlay)
    )
    Column(
        Modifier
            .align(Alignment.CenterEnd)
            .width(220.dp)
            .padding(16.dp),
        horizontalAlignment = Alignment.End
    ) {
        Row(
            Modifier
                .paint(
                    painterResource(R.drawable.bg_cinemix_favorite),
                    contentScale = ContentScale.FillBounds
                )
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painterResource(R.drawable.ic_cinemix_military), null, Modifier.size(24.dp))
            Text(
                stringResource(R.string.cinemix_editors_choice),
                color = Color(0xFFFFF1F1),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
        Text(
            movie.title,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 12.dp)
        )
        Row(
            Modifier
                .padding(top = 16.dp)
                .height(48.dp)
                .drawableBackground(R.drawable.bg_age_button_primary)
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painterResource(R.drawable.ic_cinemix_play), null, Modifier.size(24.dp))
            Text(
                stringResource(R.string.action_play_now),
                color = Color.White,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
private fun MovieSection(
    title: String, movies: List<Movie>, onMovieClick: (Long) -> Unit, onSeeAll: () -> Unit
) = Column {
    SectionHeader(title, onSeeAll)
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(movies, key = { it.id }) { movie ->
            MovieCard(
                movie = movie,
                onClick = { onMovieClick(movie.id) },
                modifier = Modifier.width(120.dp),
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String, onSeeAll: (() -> Unit)? = null) = Row(
    Modifier
        .fillMaxWidth()
        .height(48.dp)
        .padding(horizontal = 16.dp),
    verticalAlignment = Alignment.CenterVertically,
) {
    Image(
        painterResource(R.drawable.ic_cinemix_section_marker),
        null,
        Modifier
            .width(8.dp)
            .height(18.dp)
    )
    Text(
        title,
        color = Color.White,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
            .padding(start = 8.dp)
            .weight(1f)
    )
    if (onSeeAll != null) Row(
        Modifier
            .width(95.dp)
            .height(44.dp)
            .Tap(onClick = onSeeAll)
            .paint(painterResource(R.drawable.bg_see_all), contentScale = ContentScale.FillBounds),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(R.string.action_see_all),
            color = Accent,
            style = MaterialTheme.typography.labelLarge
        )
        Image(painterResource(R.drawable.ic_cinemix_arrow_right), null, Modifier.size(24.dp))
    }
}

@Preview(name = "Cinemix - Content", showBackground = true, showSystemUi = true)
@Composable
private fun CinemixContentPreview() = PreviewTheme {
    CinemixContent(
        state = CinemixUiState(isLoading = false, content = previewHomeContent),
        onRetry = {},
        onMovieClick = {},
        onFavoriteClick = {},
        onSearchClick = {},
        onCategoryClick = {},
        onSeeAllClick = {},
    )
}

@Preview(name = "Cinemix - Loading", showBackground = true)
@Composable
private fun CinemixLoadingPreview() = PreviewTheme {
    CinemixContent(CinemixUiState(), {}, {}, {}, {}, {}, {})
}
