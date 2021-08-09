package com.example.moviedbapp.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(indices = [Index(value = ["id"], unique = true)])
data class Result(
    @PrimaryKey(autoGenerate = true)
    val mIndex: Int,
    val adult: Boolean?,
    val backdrop_path: String?,
    val id: Int?,
    val original_language: String?,
    val original_title: String?,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String?,
    val release_date: String?,
    val title: String?,
    val video: Boolean?,
    val vote_average: Double?,
    val vote_count: Int?,
    val generes: String?
)
