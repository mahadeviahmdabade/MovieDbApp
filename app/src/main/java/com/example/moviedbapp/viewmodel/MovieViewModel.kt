package com.example.moviedbapp.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moviedbapp.db.MovieDatabase
import com.example.moviedbapp.models.Movies
import com.example.moviedbapp.models.Result
import com.example.moviedbapp.network.ApiService
import com.example.moviedbapp.network.RetroClient
import com.example.moviedbapp.ui.AppConstants
import com.example.moviedbapp.utility.Utils
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MovieViewModel(private val context: Application) : AndroidViewModel(context) {

    init {
        //calling default
        fetchMovies(1)
    }

    private val movies = MutableLiveData<Movies>()
    private val error = MutableLiveData<String>()
    private val results = MutableLiveData<List<Result>>()

    fun fetchMovies(page: Int) {
        //fetching from server if network available
        if (Utils.isNetworkAvailable(context)) {
            //fetching data from network
            val retroClient = RetroClient.getRetrofitInstance().create(ApiService::class.java)
            viewModelScope.launch {
                try {
                    val api = retroClient.getAllMovies(AppConstants.mApiKey, page)
                    if (api.isSuccessful) {
                        movies.value = api.body()
                        cacheRecords(api.body())
                    } else {
                        //Handling error
                        error.value = api.message()
                    }
                } catch (e: Exception) {
                    error.value = e.localizedMessage
                }

            }
        } else {
            //fetching from database if no network
            try {
                val executor: Executor = Executors.newSingleThreadExecutor()
                executor.execute {
                    val db = MovieDatabase.getAppDataBase(context)
                    results.postValue(db?.getMovieDao()?.getMovie())
                }
            } catch (e: Exception) {
                Log.e("Error", e.localizedMessage)
            }
        }
    }

    /**
     * Saving records in database for offline purpose
     *
     * @param movies
     */
    private fun cacheRecords(movies: Movies?) {
        try {
            val executor: Executor = Executors.newSingleThreadExecutor()
            executor.execute {
                val db = MovieDatabase.getAppDataBase(context)
                if (movies != null) {
                    db?.getMovieDao()?.insertMovies(movies.results)
                }
            }
        } catch (e: Exception) {
            Log.e("Error", e.localizedMessage)
        }

    }

    fun getMovies(): LiveData<Movies> {
        return movies
    }

    fun getMovieFromDb(): LiveData<List<Result>> {
        return results
    }
}