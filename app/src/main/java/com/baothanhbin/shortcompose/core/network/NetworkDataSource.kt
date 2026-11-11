package com.baothanhbin.shortcompose.core.network

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.sulotion.security.KeyStoreManager
import com.sulotion.security.NativeSecurity
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

/** Ktor counterpart of ASD113's SeriesService. A new signed header set is made per request. */
class NetworkDataSource(context: Context) {
    private val appContext = context.applicationContext
    private val config = ApiConfigManager(appContext)

    suspend fun getAllMoviesByPage(page: Int = 0, size: Int = 20): FilmPageResponse =
        request("short-dram/movies/page") {
            parameter("page", page)
            parameter("size", size)
        }

    suspend fun getTopViewedMovies(): List<FilmListResponse> = request("short-dram/movies/top-viewed")
    suspend fun getTopRatedMovies(): List<FilmListResponse> = request("short-dram/movies/top-rated")
    suspend fun getTopSearchedMovies(): List<FilmListResponse> = request("short-dram/movies/top-searched")
    suspend fun getCategoryNames(): List<String> = request("short-dram/categories/names")
    suspend fun getMovieDetail(id: Long): FilmDetailResponse = request("short-dram/movies/$id")

    suspend fun getMoviesByCategory(name: String, page: Int = 0, size: Int = 20): FilmPageResponse =
        request("short-dram/movies/by-category") {
            parameter("name", name)
            parameter("page", page)
            parameter("size", size)
        }

    suspend fun searchMovies(keyword: String, page: Int = 0, size: Int = 20): FilmPageResponse =
        request("short-dram/movies/search") {
            parameter("name", keyword)
            parameter("page", page)
            parameter("size", size)
        }

    suspend fun fetchStartupMessage(): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val firstPage = getAllMoviesByPage()
            "Loaded ${firstPage.items.size} of ${firstPage.totalElements} movies"
        }
    }

    private suspend inline fun <reified T> request(
        path: String,
        crossinline configure: HttpRequestBuilder.() -> Unit = {},
    ): T = NetworkClients.client().get(path) {
        val headers = createSecurityHeaders()
        header("X-Api-Key-Token", headers.apiKeyToken)
        header("X-Package-Name", headers.packageName)
        header("X-Device-Id", headers.deviceId)
        header("X-Request-Id", headers.requestId)
        header("X-Timestamp", headers.timestamp)
        configure()
    }.body()

    private fun createSecurityHeaders(): KeyStoreManager.TimeBoundApiHeaders =
        KeyStoreManager.createHeader(
            apiKey = config.apiKeyToken,
            packageName = config.requestPackageName.ifBlank { appContext.packageName },
            deviceId = deviceId(),
            requestId = UUID.randomUUID().toString(),
            timestampMillis = NativeSecurity.getCurrentTimeMillis(),
            apiKeyEncryption = config.apiKeyEncryption,
        )

    @SuppressLint("HardwareIds")
    private fun deviceId(): String =
        Settings.Secure.getString(appContext.contentResolver, Settings.Secure.ANDROID_ID)
            ?: "unknown-device"
}
