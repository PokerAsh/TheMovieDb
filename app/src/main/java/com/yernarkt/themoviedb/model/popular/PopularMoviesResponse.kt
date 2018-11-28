package com.yernarkt.themoviedb.model.popular

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.yernarkt.themoviedb.model.MoviesResult

abstract class MoviesResponse{
    @SerializedName("page")
    @Expose
    var page: Int? = null
    @SerializedName("total_results")
    @Expose
    var totalResults: Int? = null
    @SerializedName("total_pages")
    @Expose
    var totalPages: Int? = null
    @SerializedName("results")
    @Expose
    var results: List<MoviesResult>? = null
}