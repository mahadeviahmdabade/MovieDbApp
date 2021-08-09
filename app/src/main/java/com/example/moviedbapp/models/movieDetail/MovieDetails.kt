package com.example.moviedbapp.models.movieDetail

data class MovieDetails(
    val backdrop_path: String,
    val poster_path: String,
    val vote_average: Double,
    val release_date: String,
    val overview: String,

    val title: String,
    val adult: Boolean,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String,
    val id: Int,
    val imdb_id: String,
    val original_language: String,
    val original_title: String
)