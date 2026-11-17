package com.baothanhbin.shortcompose.app

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.baothanhbin.shortcompose.core.network.NetworkClients
import com.baothanhbin.shortcompose.core.worker.HomeContentSyncWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import com.baothanhbin.shortcompose.feature.video_player.VideoCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@HiltAndroidApp
class MainApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        NetworkClients.initialize(this)
        HomeContentSyncWorker.schedule(this)
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            VideoCache.warmUp(this@MainApplication)
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
