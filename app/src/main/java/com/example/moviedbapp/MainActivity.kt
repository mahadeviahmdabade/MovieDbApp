package com.example.moviedbapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedbapp.databinding.ActivityMainBinding
import com.example.moviedbapp.models.Movies
import com.example.moviedbapp.models.adapter.MovieAdapter
import com.example.moviedbapp.ui.InfiniteScrollingListner
import com.example.moviedbapp.ui.MovieItemClick
import com.example.moviedbapp.view.MovieDetailsActivity
import com.example.moviedbapp.viewmodel.MovieViewModel

class MainActivity : AppCompatActivity() , MovieItemClick {
    private lateinit var mMovieViewmodel: MovieViewModel
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mAdapter: MovieAdapter
    private lateinit var endlessScroller: InfiniteScrollingListner
    private lateinit var gridLayoutManager: GridLayoutManager

    private var mTotalPages = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        //Initializing Views and Components.
       setSupportActionBar(mBinding.toolbar)
        mMovieViewmodel = ViewModelProvider(this).get(MovieViewModel::class.java)
        gridLayoutManager = GridLayoutManager(this, 1)
        mBinding.moviesList.layoutManager = gridLayoutManager

        //Initializing endless scroll for movie list
        endlessScroller = object : InfiniteScrollingListner(gridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                if (page + 1 <= mTotalPages)
                    loadMoreMovies(page + 1)
                Log.d("On Scroll Change", "Load More")
            }

        }
        mBinding.moviesList.addOnScrollListener(endlessScroller)

        //Initializing movie adapter
        mAdapter = MovieAdapter(this, ArrayList(), this)
        mBinding.moviesList.adapter = mAdapter
        mBinding.moviesList.setHasFixedSize(true)

        //Attaching  all observer
        attachObserver()

    }

    private val moviesObserver = Observer<Movies> { movies ->
        mTotalPages = movies.total_pages
        //Adding data in movie list adapter
        mAdapter.addItems(movies.results)
    }

    private fun attachObserver() {
        //movies list observer.
        mMovieViewmodel.getMovies().observe(this, moviesObserver)

        //getting movies from database observer
        mMovieViewmodel.getMovieFromDb().observe(this, Observer {
            mAdapter.addItems(it)
        })
    }

    private fun loadMoreMovies(page: Int) {
        //loading more movie on scroll based on search/normal
        mMovieViewmodel.fetchMovies(page + 1)
    }

    override fun onItemClick(position: Int, movieId: Int) {
        //onClick of movie list item opening details activity
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra("MovieId", movieId)
        startActivity(intent)
    }
}