package com.baothanhbin.shortcompose.core.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import com.baothanhbin.shortcompose.core.data.repository.AppRepository

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val repository: AppRepository,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        return repository.refreshAppBootstrap()
            .fold(
                onSuccess = { Result.success() },
                onFailure = { Result.retry() },
            )
    }
}
