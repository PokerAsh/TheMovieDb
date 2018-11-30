package com.yernarkt.themoviedb.model.detail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Crew(
    @SerializedName("credit_id")
    @Expose
    var creditId: String? = null,
    @SerializedName("department")
    @Expose
    var department: String? = null,
    @SerializedName("gender")
    @Expose
    var gender: Long? = null,
    @SerializedName("id")
    @Expose
    var id: Long? = null,
    @SerializedName("job")
    @Expose
    var job: String? = null,
    @SerializedName("name")
    @Expose
    var name: String? = null,
    @SerializedName("profile_path")
    @Expose
    var profilePath: Any? = null
)