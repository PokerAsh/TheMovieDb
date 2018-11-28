package com.yernarkt.themoviedb.view

import android.support.v7.widget.RecyclerView
import com.yernarkt.themoviedb.adapter.MoviesAdapter

interface IBaseView{
    fun setVisibilityProgressBar(visibility: Int)
    fun setRecyclerViewAdapter(adapter: RecyclerView.Adapter<*>)
}