package com.baothanhbin.shortcompose.core.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

data class AppSettingsState(
    val onboardingCompleted: Boolean = false,
)

@Singleton
class AppSettingsDataStore @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val appContext = context.applicationContext
    private val appSettingsDataStore: DataStore<AppSettingsProto> = appContext.appSettingsDataStore

    val appSettings: Flow<AppSettingsState> = appSettingsDataStore.data
        .catch { error ->
            if (error is IOException) {
                emit(AppSettingsProto.getDefaultInstance())
            } else {
                throw error
            }
        }
        .map { settings ->
            AppSettingsState(
                onboardingCompleted = settings.onboardingCompleted,
            )
        }

    suspend fun getAppSettings(): AppSettingsState = appSettings.first()

    suspend fun updateOnboardingCompleted(value: Boolean) {
        appSettingsDataStore.updateData { current ->
            current.toBuilder()
                .setOnboardingCompleted(value)
                .build()
        }
    }
}

private object AppSettingsProtoSerializer : Serializer<AppSettingsProto> {
    override val defaultValue: AppSettingsProto = AppSettingsProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AppSettingsProto {
        return try {
            AppSettingsProto.parseFrom(input)
        } catch (error: IOException) {
            throw CorruptionException("Cannot read app settings proto.", error)
        }
    }

    override suspend fun writeTo(t: AppSettingsProto, output: OutputStream) {
        t.writeTo(output)
    }
}

private val Context.appSettingsDataStore: DataStore<AppSettingsProto> by dataStore(
    fileName = "app_settings.pb",
    serializer = AppSettingsProtoSerializer,
)
