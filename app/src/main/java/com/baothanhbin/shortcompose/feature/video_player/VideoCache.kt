@file:androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)

package com.baothanhbin.shortcompose.feature.video_player

import android.content.Context
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File
import java.util.concurrent.atomic.AtomicReference

object VideoCache {
    private val cache = AtomicReference<SimpleCache?>()

    /**
     * Opens the cache on a worker thread before a player is needed.  SimpleCache
     * may scan its directory and open its database, both of which are noticeable
     * when done during a navigation frame.
     */
    fun warmUp(context: Context) {
        if (cache.get() != null) return
        synchronized(this) {
            if (cache.get() == null) cache.set(createCache(context.applicationContext))
        }
    }

    fun dataSourceFactory(context: Context): CacheDataSource.Factory {
        val appContext = context.applicationContext
        // Do not initialize the disk cache from the UI thread. Playback can
        // start immediately with the upstream source on an extremely early tap;
        // later player sessions will use the warmed cache.
        val instance = cache.get() ?: return CacheDataSource.Factory()
            .setUpstreamDataSourceFactory(DefaultDataSource.Factory(appContext))
        return CacheDataSource.Factory()
            .setCache(instance)
            .setUpstreamDataSourceFactory(DefaultDataSource.Factory(appContext))
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }

    private fun createCache(context: Context) = SimpleCache(
        File(context.cacheDir, "media_cache"),
        LeastRecentlyUsedCacheEvictor(300L * 1024 * 1024),
        StandaloneDatabaseProvider(context),
    )
}
