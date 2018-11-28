package com.yernarkt.themoviedb.network

import com.yernarkt.themoviedb.model.MoviesResponse
import com.yernarkt.themoviedb.model.genres.GenreResponse
import com.yernarkt.themoviedb.model.upcoming.UpcomingMoviesResponse
import com.yernarkt.themoviedb.util.*
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET(MOVIE_POPULAR)
    fun getPopularMovieList(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Observable<MoviesResponse>

    @GET(MOVIE_UPCOMING)
    fun getUpcomingMovieList(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Observable<UpcomingMoviesResponse>

    @GET(GENRE_MOVIE_LIST)
    fun getGenreMovieList(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Observable<GenreResponse>
}