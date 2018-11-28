package com.yernarkt.themoviedb.network

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.yernarkt.themoviedb.util.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceGenerator {
    private const val HTTP_TIMEOUT: Long = 900000
    private lateinit var sServiceService: RetrofitService

    fun getRetrofitService(): RetrofitService {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .readTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
            .connectTimeout(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()

        sServiceService = retrofit.create(RetrofitService::class.java)
        return sServiceService
    }
}