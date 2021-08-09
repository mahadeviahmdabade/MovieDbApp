package com.example.moviedbapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moviedbapp.db.MovieDatabase
import com.example.moviedbapp.models.movieDetail.MovieDetails
import com.example.moviedbapp.network.ApiService
import com.example.moviedbapp.network.RetroClient
import com.example.moviedbapp.ui.AppConstants
import com.example.moviedbapp.utility.Utils
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MovieDetailsViewModel(private val context: Application) : AndroidViewModel(context) {

    private val movieDetails = MutableLiveData<MovieDetails>()
    private val apiError = MutableLiveData<String>()

    fun fetchMovieDetails(movieId: Int) {

        if (Utils.isNetworkAvailable(context)) {
            val retroClient = RetroClient.getRetrofitInstance().create(ApiService::class.java)
            viewModelScope.launch {
                try {
                    val api = retroClient.getMovieDetails(movieId, AppConstants.mApiKey)
                    if (api.isSuccessful) {
                        movieDetails.value = api.body()
                    } else {
                        apiError.value = api.message()
                    }
                } catch (e: Exception) {
                    apiError.value = e.localizedMessage.toString()
                }
            }
        } else {
            try {
                val executor: Executor = Executors.newSingleThreadExecutor()
                executor.execute {
                    val db = MovieDatabase.getAppDataBase(context)
                    val result = db!!.getMovieDao().getMovieDetails(movieId)

                    val finalResult = MovieDetails(
                        result.backdrop_path!!,
                        result.poster_path!!,
                        result.vote_average!!,
                        result.release_date!!,
                        result.overview!!,
                        result.title!!,
                        false, 0, emptyList(), "", result.id!!,
                        "", "", ""
                    )
                    movieDetails.postValue(finalResult)


                }
            } catch (e: Exception) {
                Log.e("Error", e.localizedMessage)
            }
        }
    }

    fun getMovieDetails(): LiveData<MovieDetails> {
        return movieDetails
    }

    fun getApiError(): LiveData<String> {
        return apiError
    }
}