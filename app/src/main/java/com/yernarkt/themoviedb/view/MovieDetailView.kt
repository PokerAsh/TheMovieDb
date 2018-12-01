package com.yernarkt.themoviedb.view

import com.yernarkt.themoviedb.model.detail.MovieResultDetail

interface MovieDetailView : IBaseView {
//    fun setMovieDetail(data: MovieResultDetail)
    fun setMovieCredit(cast: String)
}