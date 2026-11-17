package com.baothanhbin.shortcompose.core.data.repository

import com.baothanhbin.shortcompose.core.model.Episode
import com.baothanhbin.shortcompose.core.model.HomeContent
import com.baothanhbin.shortcompose.core.model.Movie

interface MovieRepository {
    suspend fun getHomeContent(language: String = "en"): HomeContent
    suspend fun refreshHomeContent(language: String = "en"): HomeContent
    suspend fun getMoviesByCategory(category: String, language: String = "en"): List<Movie>
    suspend fun searchMovies(query: String, language: String = "en"): List<Movie>
    suspend fun getTrending(language: String = "en"): List<Movie>
    suspend fun getNewReleases(language: String = "en"): List<Movie>
    suspend fun getSpotlight(language: String = "en"): List<Movie>
    suspend fun getMovie(movieId: Long, language: String = "en"): Movie?
    suspend fun getEpisodes(movieId: Long, language: String = "en"): List<Episode>
}
