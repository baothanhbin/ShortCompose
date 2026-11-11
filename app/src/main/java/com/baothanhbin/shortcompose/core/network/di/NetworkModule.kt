package com.baothanhbin.shortcompose.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import com.baothanhbin.shortcompose.core.network.NetworkDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideNetworkDataSource(
        @ApplicationContext context: Context,
    ): NetworkDataSource = NetworkDataSource(context)
}
