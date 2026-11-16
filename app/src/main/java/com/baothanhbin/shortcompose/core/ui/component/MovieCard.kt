package com.baothanhbin.shortcompose.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.baothanhbin.shortcompose.R
import com.baothanhbin.shortcompose.core.ui.asdTap
import com.baothanhbin.shortcompose.core.ui.drawableBackground
import coil.compose.AsyncImage
import com.baothanhbin.shortcompose.core.model.Movie

@Composable
fun MovieCard(movie: Movie, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.asdTap(onClick = onClick)) {
        Box(
            Modifier.fillMaxWidth().aspectRatio(2f / 3f).clip(RoundedCornerShape(16.dp))
                .background(Brush.verticalGradient(listOf(Color(0xFF6A3540), Color(0xFF17090D)))),
        ) {
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize(),
            )
            if (movie.viewCount > 0 && movie.currentEpisodeNumber == null) {
                Row(
                    modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp)
                        .padding(horizontal = 2.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(painterResource(R.drawable.ic_cinemix_play), null, Modifier.size(24.dp))
                    Text(movie.viewCount.compact(), color = Color.White, style = MaterialTheme.typography.bodyMedium)
                }
            }
            movie.currentEpisodeNumber?.let { episode ->
                Row(
                    Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(48.dp)
                        .drawableBackground(R.drawable.bg_playlist_card_overlay)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        stringResource(R.string.player_episode_progress, episode, movie.totalEpisodeCount),
                        color = Color.White.copy(alpha = .8f),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f),
                    )
                    Image(painterResource(R.drawable.ic_cinemix_play), null, Modifier.size(24.dp))
                    if (movie.viewCount > 0) Text(movie.viewCount.compact(), color = Color.White, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        Row(Modifier.fillMaxWidth().height(40.dp), verticalAlignment = Alignment.CenterVertically) {
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
    this >= 1_000_000 -> "%.1fM".format(this / 1_000_000f)
    this >= 1_000 -> "%.1fK".format(this / 1_000f)
    else -> toString()
}
