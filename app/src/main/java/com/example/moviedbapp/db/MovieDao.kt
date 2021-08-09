package com.example.moviedbapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviedbapp.models.Result

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movie: List<Result>)

    @Query("select * from result LIMIT 10")
    fun getMovie(): List<Result>

    @Query("select * from result where id=:movieId")
    fun getMovieDetails(movieId: Int) : Result

}