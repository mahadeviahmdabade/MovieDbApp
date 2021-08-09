package com.example.moviedbapp.models

import com.google.gson.annotations.SerializedName

data class Movies(
    @SerializedName("page")
    val page: Int,
    val results: List<Result>,
    @SerializedName("total_pages")
    val total_pages: Int,
    @SerializedName("total_results")
    val total_results: Int
)