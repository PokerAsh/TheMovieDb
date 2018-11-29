package com.yernarkt.themoviedb.viewHolders

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.yernarkt.themoviedb.R
import com.yernarkt.themoviedb.model.genres.Genre
import com.yernarkt.themoviedb.ui.activities.MovieBaseActivity
import com.yernarkt.themoviedb.ui.fragment.GenresListFragment
import com.yernarkt.themoviedb.ui.fragment.MoviesListFragment

class GenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val genreName = itemView.findViewById<TextView>(R.id.genreName)
    private val genreImageView = itemView.findViewById<ImageView>(R.id.genreDetail)

    fun bind(context: Context, genre: Genre) {
        genreName.text = genre.name

        genreImageView.setOnClickListener {
            val activity = context as MovieBaseActivity
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.activity_container, MoviesListFragment.newInstance("Genre", genre.id.toString()))
                .addToBackStack("Genre")
                .commit()
        }
    }
}