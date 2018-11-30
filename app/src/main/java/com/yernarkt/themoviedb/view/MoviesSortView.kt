package com.yernarkt.themoviedb.view

import com.yernarkt.themoviedb.model.genres.Genre

interface MoviesSortView {
    fun setGenres(genre: ArrayList<Genre>)
}