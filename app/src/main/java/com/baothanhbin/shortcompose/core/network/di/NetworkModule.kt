package com.baothanhbin.shortcompose.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.baothanhbin.shortcompose.core.network.NetworkDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideNetworkDataSource(): NetworkDataSource = NetworkDataSource
}
