package com.baothanhbin.shortcompose.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal object NetworkClients {
    private fun createClient(baseUrl: String): HttpClient {
        return HttpClient(Android) {
            defaultRequest {
                url(baseUrl)
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 60_000
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        encodeDefaults = true
                    },
                )
            }
        }
    }

    const val startupInfoUrl = "https://api.example.com/v1/startup"
    const val healthCheckUrl = "https://api.example.com/v1/health"

    val startupClient = createClient(startupInfoUrl)
    val healthCheckClient = createClient(healthCheckUrl)
}
