package com.baothanhbin.shortcompose.feature.cinemix.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Bg = Color(0xFF130A0B)
private val PeakRed = Color(0xFFEA7B71)

private data class MovieUi(
    val id: String,
    val title: String,
    val rating: String = "4.8",
    val views: String = "12.4M",
    val colors: List<Color>,
)

private val banners = listOf(
    MovieUi("b1", "The Secret Wife", "4.9", "18.2M", listOf(Color(0xFF652821), Color(0xFF120809))),
    MovieUi(
        "b2", "Love Behind the Mask", "4.8", "15.8M", listOf(Color(0xFF4D1B34), Color(0xFF11080F))
    ),
    MovieUi(
        "b3", "A Dangerous Promise", "4.7", "12.1M", listOf(Color(0xFF253F56), Color(0xFF090E14))
    ),
)

private val movies = listOf(
    MovieUi("m1", "The Contract Bride", colors = listOf(Color(0xFF7A2A31), Color(0xFF1D0B0E))),
    MovieUi("m2", "His Hidden Love", colors = listOf(Color(0xFF59355E), Color(0xFF140E18))),
    MovieUi("m3", "Never Let Me Go", colors = listOf(Color(0xFF295563), Color(0xFF0B161B))),
    MovieUi("m4", "The Last Heiress", colors = listOf(Color(0xFF70451F), Color(0xFF1B1007))),
    MovieUi("m5", "Revenge of the Queen", colors = listOf(Color(0xFF6C233D), Color(0xFF1A0710))),
)

@Composable
fun CinemixContent(title: String, modifier: Modifier = Modifier) {
    val categories = remember { listOf("Romance", "Drama", "Action", "Family", "Comedy") }
    val pagerState = rememberPagerState { banners.size }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Bg)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 108.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        TopBar(title)
        BannerPager(pagerState)
        CategorySection(categories)
        movieSection("Trending now", movies)
        movieSection("New releases", movies.reversed())
        movieSection("Spotlight", movies.drop(1) + movies.take(1))
        EditorChoiceCard(banners.first())
    }
}

@Composable
private fun TopBar(title: String) = Row(
    Modifier
        .fillMaxWidth()
        .height(64.dp)
        .padding(horizontal = 20.dp),
    verticalAlignment = Alignment.CenterVertically,
) {
    Text(
        title,
        color = Color.White,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.weight(1f)
    )
    Text(
        "⌕",
        color = Color.White,
        fontSize = 32.sp,
        modifier = Modifier
            .size(48.dp)
            .clickable { }
            .padding(bottom = 5.dp))
}

@Composable
private fun BannerPager(pagerState: PagerState) = Column {
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 16.dp),
        pageSpacing = 12.dp,
    ) { BannerCard(banners[it]) }
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp), horizontalArrangement = Arrangement.Center
    ) {
        repeat(banners.size) { index ->
            Box(
                Modifier
                    .padding(horizontal = 3.dp)
                    .width(if (index == pagerState.currentPage) 18.dp else 6.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(99.dp))
                    .background(
                        if (index == pagerState.currentPage) PeakRed else Color.White.copy(
                            .28f
                        )
                    ),
            )
        }
    }
}

@Composable
private fun BannerCard(movie: MovieUi) = Box(
    Modifier
        .fillMaxWidth()
        .height(220.dp)
        .clip(RoundedCornerShape(24.dp))
        .background(Brush.linearGradient(movie.colors)),
) {
    PosterTexture(Modifier.matchParentSize())
    Pill(
        "FOR YOU", Modifier
            .align(Alignment.TopStart)
            .padding(10.dp)
    )
    Pill(
        "★ ${movie.rating}", Modifier
            .align(Alignment.TopEnd)
            .padding(10.dp)
    )
    Row(
        Modifier
            .align(Alignment.BottomStart)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "▶  Watch now",
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .height(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(PeakRed)
                .clickable { }
                .padding(horizontal = 18.dp, vertical = 14.dp))
        Spacer(Modifier.width(8.dp))
        Text(
            "♡",
            color = Color.White,
            fontSize = 26.sp,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x55000000))
                .clickable { }
                .padding(start = 11.dp, top = 7.dp))
    }
}

@Composable
private fun CategorySection(categories: List<String>) = Column {
    SectionHeader("Feel the vibes")
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            Text(
                category,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .height(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(.12f))
                    .clickable { }
                    .padding(horizontal = 16.dp, vertical = 10.dp))
        }
    }
}

@Composable
private fun movieSection(title: String, movies: List<MovieUi>) = Column {
    SectionHeader(title)
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(movies, key = { it.id }) { MovieCard(it) }
    }
}


@Composable
private fun SectionHeader(title: String) = Row(
    Modifier
        .fillMaxWidth()
        .height(48.dp)
        .padding(horizontal = 16.dp),
    verticalAlignment = Alignment.CenterVertically,
) {
    Box(
        Modifier
            .width(8.dp)
            .height(18.dp)
            .clip(RoundedCornerShape(99.dp))
            .background(PeakRed)
    )
    Text(
        title,
        color = Color.White,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .weight(1f)
            .padding(start = 8.dp)
    )
    Text(
        "See all ›",
        color = PeakRed,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.clickable { })
}

@Composable
private fun MovieCard(movie: MovieUi) = Column(
    Modifier
        .width(120.dp)
        .clickable { }) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.verticalGradient(movie.colors))
    ) {
        PosterTexture(Modifier.matchParentSize())
        Text(
            "▶ ${movie.views}",
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
                .background(Color(0x66000000), RoundedCornerShape(10.dp))
                .padding(horizontal = 6.dp, vertical = 4.dp)
        )
    }
    Text(
        movie.title,
        color = Color.White,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
private fun EditorChoiceCard(movie: MovieUi) = Box(
    Modifier
        .fillMaxWidth()
        .height(202.dp)
        .padding(horizontal = 16.dp)
        .clip(RoundedCornerShape(20.dp))
        .background(Brush.horizontalGradient(movie.colors)),
) {
    PosterTexture(Modifier.matchParentSize())
    Column(
        Modifier
            .align(Alignment.CenterEnd)
            .width(220.dp)
            .fillMaxSize()
            .background(
                Brush.horizontalGradient(
                    0f to Color.Transparent, .42f to Color(0x6613090B), 1f to Color(0xE013090B)
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Pill("✦  EDITOR'S CHOICE")
        Text(
            movie.title,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            "▶  Play now",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .height(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(PeakRed)
                .clickable { }
                .padding(horizontal = 22.dp, vertical = 14.dp))
    }
}

@Composable
private fun Pill(text: String, modifier: Modifier = Modifier) = Text(
    text, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold,
    modifier = modifier
        .clip(RoundedCornerShape(12.dp))
        .background(Color(0x66000000))
        .padding(horizontal = 8.dp, vertical = 5.dp),
)

@Composable
private fun PosterTexture(modifier: Modifier = Modifier) = Box(
    modifier.drawWithCache {
        val circles = listOf(
            Offset(size.width * .20f, size.height * .24f) to size.minDimension * .23f,
            Offset(size.width * .78f, size.height * .64f) to size.minDimension * .29f,
        )
        onDrawBehind {
            circles.forEach { (center, radius) ->
                drawCircle(
                    Color.White.copy(.07f), radius, center
                )
            }
        }
    },
)
