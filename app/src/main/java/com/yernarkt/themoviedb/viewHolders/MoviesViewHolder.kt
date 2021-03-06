package com.yernarkt.themoviedb.viewHolders

import android.content.Context
import android.graphics.Bitmap
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.yernarkt.themoviedb.R
import com.yernarkt.themoviedb.R.id.view
import com.yernarkt.themoviedb.model.MoviesResult
import com.yernarkt.themoviedb.model.detail.SimilarMoviesResult
import com.yernarkt.themoviedb.util.BASE_IMAGE_URL
import com.yernarkt.themoviedb.util.OnRecyclerViewItemClickListener

class MoviesViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private var onRecyclerViewItemClickListener: OnRecyclerViewItemClickListener? = null
    var moviesPoster: ImageView = itemView.findViewById(R.id.movieImageView)
    var moviesView: View = itemView.findViewById(R.id.movieViewBackground)
    var moviesName: TextView = itemView.findViewById(R.id.movieNameText)

    fun bind(context: Context, data: MoviesResult) {
        moviesName.text = data.title
        val options = RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .error(R.drawable.ic_no_image)
            .priority(Priority.HIGH)

        if (data.posterPath != null)
            Glide.with(context)
                .asBitmap()
                .load(String.format("%s%s", BASE_IMAGE_URL, data.posterPath))
                .apply(options)
                .into(object : BitmapImageViewTarget(moviesPoster) {
                    override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                        super.onResourceReady(bitmap, transition)
                        Palette.from(bitmap)
                            .generate { palette -> setBackgroundColor(context as AppCompatActivity, palette!!) }
                    }
                })

        itemView.setOnClickListener(this)
    }

    fun bind(context: Context, data: SimilarMoviesResult){
        moviesName.text = data.title
        val options = RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .error(R.drawable.ic_no_image)
            .priority(Priority.HIGH)

        if (data.posterPath != null)
            Glide.with(context)
                .asBitmap()
                .load(String.format("%s%s", BASE_IMAGE_URL, data.posterPath))
                .apply(options)
                .into(object : BitmapImageViewTarget(moviesPoster) {
                    override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                        super.onResourceReady(bitmap, transition)
                        Palette.from(bitmap)
                            .generate { palette -> setBackgroundColor(context as AppCompatActivity, palette!!) }
                    }
                })

        itemView.setOnClickListener(this)
    }

    fun setClick(onRecyclerViewItemClickListener: OnRecyclerViewItemClickListener){
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener
    }

    override fun onClick(v: View?) {
        onRecyclerViewItemClickListener!!.onItemClick(adapterPosition)
    }

    private fun setBackgroundColor(context: AppCompatActivity, palette: Palette) {
        moviesView.setBackgroundColor(
            palette.getVibrantColor(
                ContextCompat.getColor(context, R.color.black_translucent_60)
            )
        )
    }
}