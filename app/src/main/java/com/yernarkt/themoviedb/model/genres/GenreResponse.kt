package com.yernarkt.themoviedb.model.genres

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GenreResponse (
    @SerializedName("genres")
    @Expose
    val genres: ArrayList<Genre>? = null
)