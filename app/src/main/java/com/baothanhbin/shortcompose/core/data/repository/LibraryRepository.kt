package com.baothanhbin.shortcompose.core.data.repository

import com.baothanhbin.shortcompose.core.database.dao.LibraryDao
import com.baothanhbin.shortcompose.core.database.model.FavoriteEntity
import com.baothanhbin.shortcompose.core.database.model.PlaybackProgressEntity
import com.baothanhbin.shortcompose.core.database.model.RecentlyWatchedEntity
import com.baothanhbin.shortcompose.core.model.Movie
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LibraryRepository @Inject constructor(private val dao: LibraryDao) {
    val favorites = dao.observeFavorites()
    val recentlyWatched = dao.observeRecentlyWatched()

    fun isFavorite(episodeId: Long) = dao.observeIsFavorite(episodeId)

    suspend fun toggleFavorite(movie: Movie, episodeId: Long, currentlyFavorite: Boolean) {
        if (currentlyFavorite) dao.deleteFavorite(episodeId)
        else dao.insertFavorite(FavoriteEntity(episodeId, movie.id, movie.title, movie.posterUrl))
    }

    suspend fun markWatched(movie: Movie, episodeId: Long, episodeNumber: Int, total: Int) =
        dao.markWatched(RecentlyWatchedEntity(movie.id, episodeId, movie.title, movie.posterUrl, episodeNumber, total))

    suspend fun getProgress(episodeId: Long) = dao.getProgress(episodeId)

    suspend fun saveProgress(episodeId: Long, movieId: Long, positionMs: Long, durationMs: Long) =
        dao.saveProgress(PlaybackProgressEntity(episodeId, movieId, positionMs, durationMs))
}
