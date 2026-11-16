@file:androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)

package com.baothanhbin.shortcompose.feature.player

import android.content.Context
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

object VideoCache {
    @Volatile private var cache: SimpleCache? = null

    @Synchronized
    fun dataSourceFactory(context: Context): CacheDataSource.Factory {
        val appContext = context.applicationContext
        val instance = cache ?: SimpleCache(
            File(appContext.cacheDir, "media_cache"),
            LeastRecentlyUsedCacheEvictor(300L * 1024 * 1024),
            StandaloneDatabaseProvider(appContext),
        ).also { cache = it }
        return CacheDataSource.Factory()
            .setCache(instance)
            .setUpstreamDataSourceFactory(DefaultDataSource.Factory(appContext))
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }
}
