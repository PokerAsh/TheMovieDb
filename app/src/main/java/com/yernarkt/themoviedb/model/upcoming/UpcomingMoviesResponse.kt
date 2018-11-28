package com.yernarkt.themoviedb.model.upcoming

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.yernarkt.themoviedb.model.popular.MoviesResponse


class UpcomingMoviesResponse(
    @SerializedName("dates")
    @Expose
    var dates: Dates? = null
) : MoviesResponse()