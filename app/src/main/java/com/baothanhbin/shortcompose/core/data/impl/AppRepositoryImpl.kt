package com.baothanhbin.shortcompose.core.data.impl

import com.baothanhbin.shortcompose.core.data.repository.AppRepository
import com.baothanhbin.shortcompose.core.database.dao.SampleDao
import com.baothanhbin.shortcompose.core.datastore.AppSettingsDataStore
import com.baothanhbin.shortcompose.core.model.AppBootstrapState
import com.baothanhbin.shortcompose.core.network.NetworkDataSource
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val appSettingsDataStore: AppSettingsDataStore,
    private val sampleDao: SampleDao,
) : AppRepository {
    override suspend fun refreshAppBootstrap(): Result<AppBootstrapState> = runCatching {
        val networkMessage = networkDataSource.fetchStartupMessage()
            .getOrElse { throwable -> "Network unavailable: ${throwable.message ?: "unknown error"}" }
        val settings = appSettingsDataStore.getAppSettings()
        val cachedItemCount = sampleDao.getItemCount()

        AppBootstrapState(
            networkMessage = networkMessage,
            onboardingCompleted = settings.onboardingCompleted,
            cachedItemCount = cachedItemCount,
            refreshedAtMillis = System.currentTimeMillis(),
        )
    }
}
