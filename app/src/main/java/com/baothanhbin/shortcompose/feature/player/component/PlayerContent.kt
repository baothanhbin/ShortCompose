@file:androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)

package com.baothanhbin.shortcompose.feature.player.component

import android.content.Context
import android.net.Uri
import android.widget.SeekBar
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.res.ResourcesCompat
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.baothanhbin.shortcompose.R
import com.baothanhbin.shortcompose.core.model.Episode
import com.baothanhbin.shortcompose.core.ui.drawableBackground
import com.baothanhbin.shortcompose.core.ui.asdTap
import com.baothanhbin.shortcompose.feature.player.PlayerUiState
import com.baothanhbin.shortcompose.feature.player.VideoCache
import com.baothanhbin.shortcompose.core.ui.preview.PreviewTheme
import com.baothanhbin.shortcompose.core.ui.preview.previewEpisodes
import com.baothanhbin.shortcompose.core.ui.preview.previewMovies
import kotlinx.coroutines.delay

private enum class ControlMode { DEFAULT, SPEED, QUALITY, SUBTITLE, EPISODES }

@Composable
fun PlayerContent(
    state: PlayerUiState,
    onBack: () -> Unit,
    onEpisode: (Int) -> Unit,
    onFavorite: () -> Unit,
    onWatched: () -> Unit,
    onSaveProgress: (Long, Long) -> Unit,
) = Box(
    Modifier
        .fillMaxSize()
        .background(Color.Black)
) {
    val inspectionMode = LocalInspectionMode.current
    when {
        inspectionMode && state.episode != null -> PlayerPreviewSurface(
            state = state,
            onBack = onBack,
            onFavorite = onFavorite,
            onEpisode = onEpisode,
        )

        state.loading -> CircularProgressIndicator(
            Modifier.align(Alignment.Center), color = Color(0xFFEA7B71)
        )

        state.error != null || state.errorRes != null -> Column(
            Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(state.error ?: stringResource(state.errorRes!!), color = Color.White)
            Text(
                stringResource(R.string.action_back),
                color = Color(0xFFEA7B71),
                modifier = Modifier
                    .padding(16.dp)
                    .asdTap(onClick = onBack)
            )
        }

        state.episode != null -> EpisodeFeed(
            state, onBack, onEpisode, onFavorite, onWatched, onSaveProgress
        )
    }
}

@Composable
private fun PlayerPreviewSurface(
    state: PlayerUiState,
    onBack: () -> Unit,
    onFavorite: () -> Unit,
    onEpisode: (Int) -> Unit,
) = Box(Modifier.fillMaxSize().background(Color(0xFF251719))) {
    Text(
        stringResource(R.string.player_video_preview),
        color = Color.White.copy(alpha = .35f),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.align(Alignment.Center),
    )
    PlayerTopBar(state = state, onBack = onBack, onFavorite = onFavorite)
    PlayerBottomPanel(
        state = state,
        mode = ControlMode.DEFAULT,
        position = 42_000,
        duration = 180_000,
        error = null,
        onMode = {},
        onEpisode = onEpisode,
        onSeek = {},
        onSpeed = {},
        onQuality = {},
        onSubtitle = {},
        modifier = Modifier.align(Alignment.BottomCenter),
    )
}

@Composable
private fun EpisodeFeed(
    state: PlayerUiState,
    onBack: () -> Unit,
    onEpisode: (Int) -> Unit,
    onFavorite: () -> Unit,
    onWatched: () -> Unit,
    onSaveProgress: (Long, Long) -> Unit,
) {
    val context = LocalContext.current
    val pool = remember { PlayerPool(context) }
    val pager = rememberPagerState(initialPage = state.selectedIndex) { state.episodes.size }
    val currentPlayer = pool.current
    var controlsVisible by remember { mutableStateOf(true) }
    var mode by remember { mutableStateOf(ControlMode.DEFAULT) }
    var playing by remember { mutableStateOf(false) }
    var buffering by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var position by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }
    val playbackErrorText = stringResource(R.string.player_error_playback)

    DisposableEffect(currentPlayer) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(value: Boolean) {
                playing = value
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                buffering = playbackState == Player.STATE_BUFFERING
                if (playbackState == Player.STATE_ENDED) {
                    val next =
                        (state.selectedIndex + 1 until state.episodes.size).firstOrNull { !state.episodes[it].isLocked }
                    if (next != null) onEpisode(next) else currentPlayer.seekTo(0)
                }
            }

            override fun onPlayerError(playbackError: PlaybackException) {
                error = playbackError.message ?: playbackErrorText; controlsVisible = true
            }
        }
        currentPlayer.addListener(listener)
        onDispose {
            currentPlayer.removeListener(listener); onSaveProgress(
            currentPlayer.currentPosition.coerceAtLeast(
                0
            ), currentPlayer.duration.coerceAtLeast(0)
        ); pool.release()
        }
    }

    LaunchedEffect(state.selectedIndex, state.resumePositionMs) {
        pool.activate(state.selectedIndex, state.episodes, state.resumePositionMs)
        error = null
        if (pager.currentPage != state.selectedIndex) pager.animateScrollToPage(state.selectedIndex)
        delay(3_000)
        if (currentPlayer.isPlaying) onWatched()
    }
    LaunchedEffect(pager.settledPage) {
        val page = pager.settledPage
        if (page != state.selectedIndex) {
            if (state.episodes.getOrNull(page)?.isLocked == false) onEpisode(page)
            else pager.animateScrollToPage(state.selectedIndex)
        }
    }
    LaunchedEffect(currentPlayer, state.selectedIndex) {
        while (true) {
            position = currentPlayer.currentPosition.coerceAtLeast(0)
            duration = currentPlayer.duration.coerceAtLeast(0)
            delay(500)
        }
    }
    LaunchedEffect(controlsVisible, playing, mode) {
        if (controlsVisible && playing && mode == ControlMode.DEFAULT) {
            delay(7_000)
            controlsVisible = false
        }
    }

    Box(Modifier.fillMaxSize()) {
        VerticalPager(
            state = pager, beyondViewportPageCount = 1, userScrollEnabled = state.episodes.size > 1
        ) { page ->
            AndroidView(
                factory = {
                    PlayerView(it).apply {
                        useController = false; resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    }
                },
                update = {
                    it.player = pool.playerFor(page, state.selectedIndex)
                    it.setOnClickListener {
                        if (controlsVisible && mode != ControlMode.DEFAULT) mode =
                            ControlMode.DEFAULT
                        else controlsVisible = !controlsVisible
                    }
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
        if (controlsVisible) {
            PlayerTopBar(state, onBack, onFavorite)
            if (buffering) CircularProgressIndicator(
                Modifier
                    .align(Alignment.Center)
                    .size(48.dp), color = Color.White
            )
            else Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(64.dp)
                    .drawableBackground(R.drawable.bg_player_center_button)
                    .asdTap {
                        if (playing) currentPlayer.pause()
                        else {
                            if (currentPlayer.playbackState == Player.STATE_ENDED) currentPlayer.seekTo(
                                0
                            )
                            currentPlayer.play()
                        }
                    },
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(if (playing) R.drawable.ic_player_pause else R.drawable.ic_player_play),
                    contentDescription = stringResource(if (playing) R.string.player_pause else R.string.action_play),
                    modifier = Modifier.size(28.dp),
                )
            }
            PlayerBottomPanel(
                state = state, mode = mode, position = position, duration = duration, error = error,
                onMode = { mode = it }, onEpisode = onEpisode,
                onSeek = { currentPlayer.seekTo(it) },
                onSpeed = { currentPlayer.setPlaybackSpeed(it); mode = ControlMode.DEFAULT },
                onQuality = { height ->
                    currentPlayer.trackSelectionParameters =
                        currentPlayer.trackSelectionParameters.buildUpon()
                            .setMaxVideoSize(Int.MAX_VALUE, height).build(); mode =
                    ControlMode.DEFAULT
                },
                onSubtitle = { language ->
                    currentPlayer.trackSelectionParameters =
                        currentPlayer.trackSelectionParameters.buildUpon()
                            .setTrackTypeDisabled(C.TRACK_TYPE_TEXT, language == null)
                            .setPreferredTextLanguage(language).build()
                    mode = ControlMode.DEFAULT
                },
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
private fun PlayerTopBar(state: PlayerUiState, onBack: () -> Unit, onFavorite: () -> Unit) = Row(
    Modifier
        .fillMaxWidth()
        .padding(top = 24.dp, start = 16.dp, end = 16.dp)
        .height(64.dp)
        .drawableBackground(R.drawable.bg_player_glass_24)
        .padding(8.dp),
    verticalAlignment = Alignment.CenterVertically,
) {
    Box(
        Modifier
            .size(48.dp)
            .drawableBackground(R.drawable.bg_player_button_16)
            .asdTap(onClick = onBack),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_player_arrow_right),
            contentDescription = stringResource(R.string.action_back),
            modifier = Modifier
                .size(24.dp)
                .rotate(180f),
        )
    }
    Column(
        Modifier
            .weight(1f)
            .padding(horizontal = 8.dp)
    ) {
        Text(
            state.movie?.title.orEmpty(),
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1
        )
        Text(
            stringResource(R.string.player_episode_progress, state.episode?.number ?: 0, state.episodes.size),
            color = Color.White.copy(.7f),
            style = MaterialTheme.typography.labelMedium
        )
    }
    Box(
        Modifier
            .size(48.dp)
            .drawableBackground(R.drawable.bg_player_button_16)
            .asdTap(onClick = onFavorite),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(if (state.isFavorite) R.drawable.ic_player_favorite_filled else R.drawable.ic_player_favorite_border),
            contentDescription = stringResource(R.string.action_favorite),
            modifier = Modifier.size(24.dp),
        )
    }
}

@Composable
private fun PlayerBottomPanel(
    state: PlayerUiState,
    mode: ControlMode,
    position: Long,
    duration: Long,
    error: String?,
    onMode: (ControlMode) -> Unit,
    onEpisode: (Int) -> Unit,
    onSeek: (Long) -> Unit,
    onSpeed: (Float) -> Unit,
    onQuality: (Int) -> Unit,
    onSubtitle: (String?) -> Unit,
    modifier: Modifier = Modifier,
) = Column(
    modifier
        .fillMaxWidth()
        .padding(16.dp)
        .drawableBackground(R.drawable.bg_player_glass_16)
        .padding(12.dp),
) {
    error?.let { Text(it, color = Color(0xFFEA7B71), modifier = Modifier.padding(bottom = 8.dp)) }
    when (mode) {
        ControlMode.DEFAULT -> Row(
            Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.player_episode, state.episode?.number ?: 0),
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            ControlButton(
                R.drawable.ic_player_speed, stringResource(R.string.player_speed)
            ) { onMode(ControlMode.SPEED) }
            ControlButton(
                R.drawable.ic_player_tune, stringResource(R.string.player_quality)
            ) { onMode(ControlMode.QUALITY) }
            ControlButton(
                R.drawable.ic_player_subtitles,
                stringResource(R.string.player_subtitles),
                enabled = state.episode?.subtitles?.isNotEmpty() == true
            ) { onMode(ControlMode.SUBTITLE) }
            ControlButton(
                R.drawable.ic_player_episode_list, stringResource(R.string.player_episodes)
            ) { onMode(ControlMode.EPISODES) }
        }

        ControlMode.SPEED -> OptionRow(
            listOf(
                stringResource(R.string.player_speed_1) to 1f,
                stringResource(R.string.player_speed_125) to 1.25f,
                stringResource(R.string.player_speed_15) to 1.5f,
                stringResource(R.string.player_speed_2) to 2f,
            ), onSpeed
        )

        ControlMode.QUALITY -> OptionRow(
            listOf(
                stringResource(R.string.player_quality_1440) to 1440,
                stringResource(R.string.player_quality_1080) to 1080,
                stringResource(R.string.player_quality_720) to 720,
                stringResource(R.string.player_quality_540) to 540,
                stringResource(R.string.player_quality_360) to 360,
            ), onQuality
        )

        ControlMode.SUBTITLE -> OptionRow(
            listOf(stringResource(R.string.player_subtitles_off) to null) + state.episode.orEmptySubtitles()
                .map { it.languageName to it.languageCode },
            onSubtitle,
        )

        ControlMode.EPISODES -> LazyVerticalGrid(
            GridCells.Fixed(6),
            modifier = Modifier
                .fillMaxWidth()
                .height(168.dp),
            contentPadding = PaddingValues(top = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            itemsIndexed(state.episodes) { index, episode ->
                Box(
                    Modifier
                        .width(48.dp)
                        .height(40.dp)
                        .drawableBackground(if (episode.isLocked) R.drawable.bg_player_episode_locked else R.drawable.bg_player_episode_state)
                        .asdTap(enabled = !episode.isLocked) { onEpisode(index) },
                    contentAlignment = Alignment.Center,
                ) {
                    if (episode.isLocked) {
                        Image(
                            painterResource(R.drawable.ic_player_lock),
                            stringResource(R.string.player_locked),
                            Modifier.size(18.dp)
                        )
                    } else {
                        Text(
                            episode.number.toString(),
                            color = if (index == state.selectedIndex) Color(0xFFEA7B71) else Color.White,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
            }
        }
    }
    if (mode != ControlMode.EPISODES) {
        AndroidView(
            factory = { context ->
                SeekBar(context).apply {
                    val density = resources.displayMetrics.density
                    max = 1_000
                    progressDrawable = ResourcesCompat.getDrawable(
                        context.resources, R.drawable.progress_player_seek, context.theme
                    )
                    thumb = ResourcesCompat.getDrawable(
                        context.resources, R.drawable.thumb_player_seek, context.theme
                    )
                    splitTrack = false
                    setPadding((4 * density).toInt(), 0, (4 * density).toInt(), 0)
                }
            },
            update = { seekBar ->
                seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?, value: Int, fromUser: Boolean
                    ) {
                        if (fromUser && duration > 0) onSeek(duration * value / 1_000)
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
                    override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
                })
                val value = if (duration > 0) (position * 1_000 / duration).toInt()
                    .coerceIn(0, 1_000) else 0
                if (!seekBar.isPressed && seekBar.progress != value) seekBar.progress = value
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp),
        )
        Row(Modifier.fillMaxWidth()) {
            Text(
                position.time(),
                color = Color.White.copy(.7f),
                style = MaterialTheme.typography.labelSmall
            ); Text(
            duration.time(),
            color = Color.White.copy(.7f),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.weight(1f),
            textAlign = androidx.compose.ui.text.style.TextAlign.End
        )
        }
    }
}

@Composable
private fun ControlButton(
    @DrawableRes icon: Int,
    contentDescription: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) = Box(
    Modifier
        .size(40.dp)
        .asdTap(enabled = enabled, onClick = onClick),
    contentAlignment = Alignment.Center,
) {
    Image(
        painter = painterResource(icon),
        contentDescription = contentDescription,
        modifier = Modifier.size(24.dp),
        alpha = if (enabled) 1f else .4f,
    )
}

@Composable
private fun <T> OptionRow(options: List<Pair<String, T>>, onSelect: (T) -> Unit) =
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { (label, value) ->
            Box(
                Modifier
                    .weight(1f)
                    .height(36.dp)
                    .drawableBackground(R.drawable.bg_player_option)
                    .asdTap { onSelect(value) },
                contentAlignment = Alignment.Center,
            ) {
                Text(label, color = Color.White, style = MaterialTheme.typography.labelSmall)
            }
        }
    }

private fun Long.time(): String {
    val seconds = (this / 1_000).coerceAtLeast(0); return "%02d:%02d".format(
        seconds / 60, seconds % 60
    )
}

private fun Episode?.orEmptySubtitles() = this?.subtitles.orEmpty()

@UnstableApi
private class PlayerPool(context: Context) {
    private val mediaSourceFactory =
        DefaultMediaSourceFactory(VideoCache.dataSourceFactory(context))
    private val loadControl =
        DefaultLoadControl.Builder().setBufferDurationsMs(15_000, 50_000, 500, 1_000).build()
    private val players = List(3) {
        ExoPlayer.Builder(context).setMediaSourceFactory(mediaSourceFactory)
            .setLoadControl(loadControl).build()
    }
    val current: ExoPlayer get() = players[0]

    fun activate(index: Int, episodes: List<Episode>, resumePositionMs: Long) {
        prepare(players[0], episodes.getOrNull(index), resumePositionMs, play = true, volume = 1f)
        prepare(players[1], episodes.getOrNull(index + 1), 0, play = false, volume = 0f)
        prepare(players[2], episodes.getOrNull(index - 1), 0, play = false, volume = 0f)
    }

    fun playerFor(page: Int, currentIndex: Int): ExoPlayer? = when (page) {
        currentIndex -> players[0]
        currentIndex + 1 -> players[1]
        currentIndex - 1 -> players[2]
        else -> null
    }

    private fun prepare(
        player: ExoPlayer, episode: Episode?, position: Long, play: Boolean, volume: Float
    ) {
        if (episode == null || episode.isLocked || episode.videoUrl.isBlank()) {
            player.stop(); player.clearMediaItems(); return
        }
        player.volume = volume
        player.setMediaItem(episode.mediaItem())
        player.prepare()
        if (position > 0) player.seekTo(position)
        player.playWhenReady = play
    }

    fun release() = players.forEach(ExoPlayer::release)
}

private fun Episode.mediaItem(): MediaItem =
    MediaItem.Builder().setUri(videoUrl).setSubtitleConfigurations(subtitles.map { track ->
        MediaItem.SubtitleConfiguration.Builder(Uri.parse(track.url)).setMimeType(
            when {
                track.url.endsWith(".srt", true) -> MimeTypes.APPLICATION_SUBRIP
                track.url.endsWith(".ttml", true) -> MimeTypes.APPLICATION_TTML
                else -> MimeTypes.TEXT_VTT
            }
        ).setLanguage(track.languageCode).setLabel(track.languageName).build()
    }).build()

@Preview(name = "Player", showBackground = true, showSystemUi = true)
@Composable
private fun PlayerContentPreview() = PreviewTheme {
    PlayerContent(
        state = PlayerUiState(
            loading = false,
            movie = previewMovies.first(),
            episodes = previewEpisodes,
            selectedIndex = 2,
            isFavorite = true,
        ),
        onBack = {},
        onEpisode = {},
        onFavorite = {},
        onWatched = {},
        onSaveProgress = { _, _ -> },
    )
}

@Preview(name = "Player - Loading", showBackground = true)
@Composable
private fun PlayerLoadingPreview() = PreviewTheme {
    PlayerContent(PlayerUiState(), {}, {}, {}, {}, { _, _ -> })
}
