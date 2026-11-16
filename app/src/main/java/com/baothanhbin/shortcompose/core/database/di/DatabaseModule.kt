package com.baothanhbin.shortcompose.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.baothanhbin.shortcompose.core.database.AppDatabase
import com.baothanhbin.shortcompose.core.database.dao.SampleDao
import com.baothanhbin.shortcompose.core.database.dao.LibraryDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "shortcompose.db",
    ).fallbackToDestructiveMigration(true).build()

    @Provides
    fun provideSampleDao(database: AppDatabase): SampleDao = database.sampleDao()

    @Provides
    fun provideLibraryDao(database: AppDatabase): LibraryDao = database.libraryDao()
}
