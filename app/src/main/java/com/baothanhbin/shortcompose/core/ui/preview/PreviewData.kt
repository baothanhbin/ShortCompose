package com.baothanhbin.shortcompose.core.ui.preview

import androidx.compose.runtime.Composable
import com.baothanhbin.shortcompose.core.model.Episode
import com.baothanhbin.shortcompose.core.model.HomeContent
import com.baothanhbin.shortcompose.core.model.Movie
import com.baothanhbin.shortcompose.core.model.MovieCategory
import com.baothanhbin.shortcompose.core.theme.SingleModuleStarterTheme

val previewMovies = listOf(
    Movie(1, "The Hidden Heiress", rating = 4.8, viewCount = 1_240_000, currentEpisodeNumber = 8, totalEpisodeCount = 40),
    Movie(2, "Love After Midnight", rating = 4.7, viewCount = 860_000, isFavorite = true),
    Movie(3, "My Contract Husband", rating = 4.6, viewCount = 520_000),
    Movie(4, "Falling for the CEO", rating = 4.9, viewCount = 2_100_000),
    Movie(5, "A Second Chance", rating = 4.5, viewCount = 310_000),
    Movie(6, "Secret Vows", rating = 4.8, viewCount = 990_000),
)

val previewHomeContent = HomeContent(
    banners = previewMovies.take(3),
    categories = listOf("Romance", "Drama", "Revenge", "CEO").map { MovieCategory(it) },
    trending = previewMovies,
    newRelease = previewMovies.reversed(),
    spotlight = previewMovies.shuffled(),
)

val previewEpisodes = List(12) { index ->
    Episode(
        id = index.toLong() + 1,
        movieId = 1,
        number = index + 1,
        title = "Episode ${index + 1}",
        videoUrl = "",
        isLocked = index > 7,
    )
}

@Composable
fun PreviewTheme(content: @Composable () -> Unit) {
    SingleModuleStarterTheme(useDarkTheme = true, useDynamicColor = false, content = content)
}
