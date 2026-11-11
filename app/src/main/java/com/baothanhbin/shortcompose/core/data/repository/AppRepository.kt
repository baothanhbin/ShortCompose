package com.baothanhbin.shortcompose.core.data.repository

import com.baothanhbin.shortcompose.core.model.AppBootstrapState

interface AppRepository {
    suspend fun refreshAppBootstrap(): Result<AppBootstrapState>
}
