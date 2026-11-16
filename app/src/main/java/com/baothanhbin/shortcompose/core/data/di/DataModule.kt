package com.baothanhbin.shortcompose.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.baothanhbin.shortcompose.core.data.impl.AppRepositoryImpl
import com.baothanhbin.shortcompose.core.data.repository.AppRepository
import com.baothanhbin.shortcompose.core.data.impl.MovieRepositoryImpl
import com.baothanhbin.shortcompose.core.data.repository.MovieRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindAppRepository(
        impl: AppRepositoryImpl,
    ): AppRepository

    @Binds
    @Singleton
    abstract fun bindMovieRepository(impl: MovieRepositoryImpl): MovieRepository
}
