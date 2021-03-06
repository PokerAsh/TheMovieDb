package com.yernarkt.themoviedb.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MoviesResult(
    @SerializedName("vote_count")
    @Expose
    var voteCount: Int = 0,
    @SerializedName("id")
    @Expose
    var id: Long = 0,
    @SerializedName("video")
    @Expose
    var video: Boolean = false,
    @SerializedName("vote_average")
    @Expose
    var voteAverage: Double? = null,
    @SerializedName("title")
    @Expose
    var title: String? = null,
    @SerializedName("popularity")
    @Expose
    var popularity: Double? = null,
    @SerializedName("poster_path")
    @Expose
    var posterPath: String? = null,
    @SerializedName("original_language")
    @Expose
    var originalLanguage: String? = null,
    @SerializedName("original_title")
    @Expose
    var originalTitle: String? = null,
    @SerializedName("genre_ids")
    @Expose
    var genreIds: List<Int>? = null,
    @SerializedName("backdrop_path")
    @Expose
    var backdropPath: String? = null,
    @SerializedName("adult")
    @Expose
    var adult: Boolean = false,
    @SerializedName("overview")
    @Expose
    var overview: String? = null,
    @SerializedName("release_date")
    @Expose
    var releaseDate: String? = null
)