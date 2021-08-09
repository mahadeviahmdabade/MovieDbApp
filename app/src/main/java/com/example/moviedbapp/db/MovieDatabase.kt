package com.example.moviedbapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moviedbapp.models.Result

@Database(version = 1, entities = [Result::class], exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun getMovieDao(): MovieDao

    companion object {
        private var INSTANCE: MovieDatabase? = null

        /**
         * Creating Singletone object for Database
         * @param
         * @return
         */
        fun getAppDataBase(context: Context): MovieDatabase? {
            if (INSTANCE == null) {
                synchronized(MovieDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MovieDatabase::class.java,
                        "movie_database"
                    ).build()
                }
            }
            return INSTANCE
        }

    }
}