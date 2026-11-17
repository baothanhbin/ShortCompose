package com.baothanhbin.shortcompose.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.baothanhbin.shortcompose.R
import com.baothanhbin.shortcompose.core.model.Movie
import com.baothanhbin.shortcompose.core.utils.Tap
import com.baothanhbin.shortcompose.core.utils.drawableBackground
import java.util.Locale

@Composable
fun MovieCard(
    movie: Movie,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewCountText = movie.viewCount.takeIf { it > 0 }?.compact()
    Column(modifier.Tap(onClick = onClick)) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .aspectRatio(2f / 3f)
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.verticalGradient(listOf(Color(0xFF6A3540), Color(0xFF17090D)))),
        ) {
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize(),
            )
            if (viewCountText != null && movie.currentEpisodeNumber == null) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .padding(horizontal = 2.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(painterResource(R.drawable.ic_cinemix_play), null, Modifier.size(24.dp))
                    Text(
                        viewCountText,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            movie.currentEpisodeNumber?.let { episode ->
                Row(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(48.dp)
                        .drawableBackground(R.drawable.bg_playlist_card_overlay)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        stringResource(
                            R.string.player_episode_progress, episode, movie.totalEpisodeCount
                        ),
                        color = Color.White.copy(alpha = .8f),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f),
                    )
                    Image(painterResource(R.drawable.ic_cinemix_play), null, Modifier.size(24.dp))
                    if (viewCountText != null) Text(
                        viewCountText,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .height(40.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                movie.title,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            if (movie.currentEpisodeNumber != null || movie.isFavorite) {
                Image(
                    painterResource(if (movie.isFavorite) R.drawable.ic_cinemix_favorite_on else R.drawable.ic_cinemix_favorite_off),
                    stringResource(R.string.action_favorite),
                    Modifier.size(24.dp),
                )
            }
        }
    }
}

private fun Long.compact(): String = when {
    this >= 1_000_000 -> String.format(Locale.US, "%.1fM", this / 1_000_000.0)
    this >= 1_000 -> String.format(Locale.US, "%.1fK", this / 1_000.0)
    else -> toString()
}

@Preview(showBackground = true, backgroundColor = 0xFF17090D)
@Composable
private fun MovieCardPreview() {
    MaterialTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MovieCard(
                movie = Movie(
                    id = 1L, // Sửa thành Long
                    title = "Bí Mật Đêm Khuya",
                    posterUrl = "",
                    viewCount = 1_250_000L,
                    currentEpisodeNumber = null,
                    totalEpisodeCount = 24,
                    isFavorite = true
                ),
                onClick = {},
                modifier = Modifier.width(160.dp)
            )

            MovieCard(
                movie = Movie(
                    id = 2L, // Sửa thành Long
                    title = "Hành Trình Vô Tận",
                    posterUrl = "",
                    viewCount = 85_400L,
                    currentEpisodeNumber = 5,
                    totalEpisodeCount = 16,
                    isFavorite = false
                ),
                onClick = {},
                modifier = Modifier.width(160.dp)
            )
        }
    }
}
