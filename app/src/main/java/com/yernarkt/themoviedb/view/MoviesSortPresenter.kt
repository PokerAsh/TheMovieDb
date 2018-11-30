package com.yernarkt.themoviedb.view

import android.content.Context
import android.support.design.widget.Snackbar
import android.view.View
import com.yernarkt.themoviedb.R
import com.yernarkt.themoviedb.adapter.MoviesAdapter
import com.yernarkt.themoviedb.model.genres.Genre
import com.yernarkt.themoviedb.model.genres.GenreResponse
import com.yernarkt.themoviedb.network.ServiceGenerator
import com.yernarkt.themoviedb.util.API_KEY
import com.yernarkt.themoviedb.util.LANGUAGE
import com.yernarkt.themoviedb.viewHolders.GenreViewHolder
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MoviesSortPresenter(private val context: Context, private val moviesSortView: MoviesSortView, private val mView: View){
    fun loadGenreList() {
        val observable = ServiceGenerator.getRetrofitService().getGenreMovieList(
            API_KEY,
            LANGUAGE
        )

        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<GenreResponse> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(genreResponse: GenreResponse) {
                    if (genreResponse.genres != null) {
                        moviesSortView.setGenres(genreResponse.genres)
                    } else {
                        Snackbar.make(mView, context.getString(R.string.s_there_are_no_elements), Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onError(e: Throwable) {
                    Snackbar.make(mView, context.getString(R.string.s_on_failure_message), Snackbar.LENGTH_SHORT).show()
                    Timber.d(e.localizedMessage)
                }

                override fun onComplete() {

                }
            })
    }
}
