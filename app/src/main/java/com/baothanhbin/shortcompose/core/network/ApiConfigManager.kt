package com.baothanhbin.shortcompose.core.network

import android.content.Context
import java.util.Properties

internal class ApiConfigManager(context: Context) {
    private val properties = Properties().apply {
        context.assets.open(CONFIG_FILE).use(::load)
    }

    val baseUrl: String
        get() = properties.getProperty("base_url").orEmpty().trim().let {
            require(it.startsWith("http")) { "Invalid Short Drama API base URL" }
            if (it.endsWith('/')) it else "$it/"
        }

    val apiKeyToken: String get() = properties.getProperty("X_Api_Key_Token").orEmpty().trim()
    val apiKeyEncryption: Boolean get() = properties.getProperty("api_key_encryption", "true").trim().toBoolean()
    val requestPackageName: String get() = properties.getProperty("X_Package_Name").orEmpty().trim()

    private companion object { const val CONFIG_FILE = "api_config.properties" }
}
