package com.yernarkt.themoviedb.model.detail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SimilarMoviesResponse(
    @SerializedName("page")
    @Expose
    var page: Long? = null,
    @SerializedName("results")
    @Expose
    var results: List<SimilarMoviesResult>? = null,
    @SerializedName("total_pages")
    @Expose
    var totalPages: Long? = null,
    @SerializedName("total_results")
    @Expose
    var totalResults: Long? = null
)