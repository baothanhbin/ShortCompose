package com.baothanhbin.shortcompose.core.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.baothanhbin.shortcompose.core.data.repository.MovieRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.CancellationException

@HiltWorker
class HomeContentSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val movieRepository: MovieRepository,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val language = applicationContext
            .getSharedPreferences("locale", Context.MODE_PRIVATE)
            .getString("code", "en")
            .orEmpty()
            .ifBlank { "en" }

        return try {
            movieRepository.refreshHomeContent(language)
            Result.success()
        } catch (error: Throwable) {
            if (error is CancellationException) throw error
            Result.retry()
        }
    }

    companion object {
        private const val UNIQUE_WORK_NAME = "cinemix-home-content-sync"
        private const val SYNC_INTERVAL_HOURS = 8L

        fun schedule(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val request = PeriodicWorkRequestBuilder<HomeContentSyncWorker>(
                SYNC_INTERVAL_HOURS,
                TimeUnit.HOURS,
            )
                .setInitialDelay(SYNC_INTERVAL_HOURS, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                UNIQUE_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request,
            )
        }
    }
}
