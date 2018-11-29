package com.yernarkt.themoviedb.view

import android.content.Context
import android.view.View

class MovieDetailPresenter(private val context: Context, private val detailView: MovieDetailView){
    fun loadMovieById(id: Int){
        detailView.setVisibilityProgressBar(View.VISIBLE)
    }
}