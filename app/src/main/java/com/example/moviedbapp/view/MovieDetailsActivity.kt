package com.example.moviedbapp.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.moviedbapp.R
import com.example.moviedbapp.databinding.ActivityMovieDetailsBinding
import com.example.moviedbapp.models.movieDetail.Genre
import com.example.moviedbapp.models.movieDetail.MovieDetails
import com.example.moviedbapp.ui.AppConstants
import com.example.moviedbapp.viewmodel.MovieDetailsViewModel
import com.google.android.material.snackbar.Snackbar

class MovieDetailsActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityMovieDetailsBinding
    private lateinit var mViewmodel: MovieDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mViewmodel = ViewModelProvider(this).get(MovieDetailsViewModel::class.java)

        //getting movieid from previous activity and fetching details
        val movieId = intent.getIntExtra("MovieId", 399566)

        attachObserver()
        //showing loader
        Glide
            .with(this)
            .load(R.drawable.hour_glass)
            .into(mBinding.imageView8)
        mBinding.imageView8.visibility = View.VISIBLE

        mViewmodel.fetchMovieDetails(movieId)


        //closing activity
        mBinding.imageView6.setOnClickListener {
            this.finish()
        }


    }

    private fun attachObserver() {
        mViewmodel.getMovieDetails().observe(this, Observer {
            populateMovieDetails(it)
            mBinding.imageView8.visibility = View.GONE
            mBinding.mainContainer.visibility = View.VISIBLE

        })

        mViewmodel.getApiError().observe(this, Observer {
            showError(it)
        })
    }

    /**
     * Populating ui from movie details object
     *
     * @param movieDetails
     */
    private fun populateMovieDetails(movieDetails: MovieDetails) {
        //setting data to views
        Log.d("MovieDetails", movieDetails.title)
        Glide
            .with(this)
            .load(AppConstants.IMAGE_BACKGROUND_URL + movieDetails.backdrop_path)
            .into(mBinding.imageView2)

        Glide
            .with(this)
            .load(AppConstants.IMAGE_BASE_URL + movieDetails.poster_path)
            .into(mBinding.imageView3)

        mBinding.textView.text = movieDetails.title
        mBinding.textView3.text = movieDetails.vote_average.toString()
        mBinding.textView4.text = movieDetails.release_date
        mBinding.textView6.text = movieDetails.overview

        mBinding.textView2.text = getGenersValue(movieDetails.genres)
    }


    /**
     * generating generes value from list f genres.
     *
     * @param genres
     * @return
     */
    private fun getGenersValue(genres: List<Genre>): String {
        var name = StringBuilder()
        for (element in genres) {
            name.append(element.name + " ")
        }
        return name.toString()
    }


    private fun showError(errorMessage: String) {
        Snackbar.make(mBinding.root, errorMessage, Snackbar.LENGTH_LONG).show()
    }
}