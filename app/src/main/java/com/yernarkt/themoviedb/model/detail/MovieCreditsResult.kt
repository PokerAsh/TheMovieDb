package com.yernarkt.themoviedb.model.detail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MovieCreditsResult(
    @SerializedName("id")
    @Expose
    var id: Long? = null,
    @SerializedName("cast")
    @Expose
    var cast: List<Cast>? = null,
    @SerializedName("crew")
    @Expose
    var crew: List<Crew>? = null
)