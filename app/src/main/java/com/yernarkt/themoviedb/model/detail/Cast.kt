package com.yernarkt.themoviedb.model.detail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Cast(
    @SerializedName("cast_id")
    @Expose
    var castId: Long? = null,
    @SerializedName("character")
    @Expose
    var character: String? = null,
    @SerializedName("credit_id")
    @Expose
    var creditId: String? = null,
    @SerializedName("gender")
    @Expose
    var gender: Long? = null,
    @SerializedName("id")
    @Expose
    var id: Long? = null,
    @SerializedName("name")
    @Expose
    var name: String? = null,
    @SerializedName("order")
    @Expose
    var order: Long? = null,
    @SerializedName("profile_path")
    @Expose
    var profilePath: String? = null
)