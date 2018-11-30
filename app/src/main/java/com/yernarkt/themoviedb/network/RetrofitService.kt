package com.yernarkt.themoviedb.network

import com.yernarkt.themoviedb.model.MoviesResponse
import com.yernarkt.themoviedb.model.detail.MovieCreditsResult
import com.yernarkt.themoviedb.model.detail.MovieResultDetail
import com.yernarkt.themoviedb.model.detail.SimilarMoviesResponse
import com.yernarkt.themoviedb.model.genres.GenreResponse
import com.yernarkt.themoviedb.model.upcoming.UpcomingMoviesResponse
import com.yernarkt.themoviedb.util.*
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

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

    @GET(DISCOVER_MOVIE)
    fun getMoviesByGenre(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("sort_by") sortBy: String,
        @Query("page") page: Int,
        @Query("with_genres") genreId: String
    ): Observable<MoviesResponse>

    @GET(DISCOVER_MOVIE)
    fun getSortedMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("sort_by") sortBy: String,
        @Query("page") page: Int,
        @QueryMap queryMap: MutableMap<String, String>
    ): Observable<MoviesResponse>

    @GET(GENRE_MOVIE_LIST)
    fun getGenreMovieList(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Observable<GenreResponse>

    @GET(SEARCH_MOVIE)
    fun searchMovie(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("query") query: CharSequence?,
        @Query("page") page: Int
    ): Observable<MoviesResponse>

    @GET(MOVIE_BY_ID)
    fun getMovieById(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Observable<MovieResultDetail>

    @GET(MOVIE_CREDITS)
    fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Observable<MovieCreditsResult>

    @GET(MOVIE_SIMILAR)
    fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Observable<SimilarMoviesResponse>
}