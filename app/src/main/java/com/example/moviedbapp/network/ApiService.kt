package com.example.moviedbapp.network

import com.example.moviedbapp.models.Movies
import com.example.moviedbapp.models.movieDetail.MovieDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    /**
     * Fetching movies based on page index from server
     *
     * @param apiKey
     * @param page
     * @return
     */
    @GET("discover/movie")
    suspend fun getAllMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<Movies>

    /**
     * Fetching moviewdetails from server using movieid
     *
     * @param movieId
     * @param apiKey
     * @return
     */
    @GET("movie/{movieId}")
    suspend fun getMovieDetails(
        @Path("movieId") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<MovieDetails>

    /**
     * Fetching movies based on search key
     *
     * @param query
     * @param apiKey
     * @return
     */
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("api_key") apiKey: String
    ): Response<Movies>

}