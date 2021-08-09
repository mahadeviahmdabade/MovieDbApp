package com.example.moviedbapp.network

import com.example.moviedbapp.ui.AppConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetroClient {

    companion object {
        var retrofit: Retrofit? = null

        /**
         * Returning Retrofit singleton instance
         *
         * @return
         */
        fun getRetrofitInstance(): Retrofit {
            if (retrofit == null) {

                //adding okhttp client for setting timeout
                val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build()
                retrofit =
                    Retrofit.Builder()
                        .baseUrl(AppConstants.BASE_URL)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }
            return retrofit!!
        }
    }
}