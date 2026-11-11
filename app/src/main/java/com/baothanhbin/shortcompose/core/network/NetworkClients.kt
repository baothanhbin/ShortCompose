package com.baothanhbin.shortcompose.core.network

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/** Shared API client. Call [initialize] once from [android.app.Application.onCreate]. */
object NetworkClients {
    private var httpClient: HttpClient? = null

    fun initialize(context: Context) {
        if (httpClient == null) {
            httpClient = createClient(context.applicationContext)
        }
    }

    fun client(): HttpClient = checkNotNull(httpClient) {
        "NetworkClients must be initialized from Application.onCreate()."
    }

    private fun createClient(context: Context): HttpClient = HttpClient(Android) {
        defaultRequest {
            url(ApiConfigManager(context).baseUrl)
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 60_000
        }
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true; encodeDefaults = true })
        }
    }
}
