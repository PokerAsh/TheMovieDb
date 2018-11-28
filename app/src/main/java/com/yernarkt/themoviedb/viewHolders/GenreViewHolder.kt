package com.yernarkt.themoviedb.viewHolders

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.yernarkt.themoviedb.R
import com.yernarkt.themoviedb.model.genres.Genre

class GenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val genreName = itemView.findViewById<TextView>(R.id.genreName)
    private val genreImageView = itemView.findViewById<ImageView>(R.id.genreDetail)

    fun bind(context: Context, genre: Genre) {
        genreName.text = genre.name

        genreImageView.setOnClickListener {
            Toast.makeText(context, String.format("%s", genre.id), Toast.LENGTH_LONG).show()
        }
    }
}