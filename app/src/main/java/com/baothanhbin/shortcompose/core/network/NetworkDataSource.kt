package com.baothanhbin.shortcompose.core.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object NetworkDataSource {
    suspend fun fetchStartupMessage(): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val startupEngine = NetworkClients.startupClient.engine::class.simpleName ?: "UnknownEngine"
            "Startup API: ${NetworkClients.startupInfoUrl} | Health API: ${NetworkClients.healthCheckUrl} | engine: $startupEngine"
        }
    }
}
