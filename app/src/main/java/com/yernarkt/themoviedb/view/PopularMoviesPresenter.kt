package com.yernarkt.themoviedb.view

import android.content.Context
import android.support.design.widget.Snackbar
import android.view.View
import com.yernarkt.themoviedb.R
import com.yernarkt.themoviedb.adapter.MoviesAdapter
import com.yernarkt.themoviedb.model.PopularMoviesResponse
import com.yernarkt.themoviedb.model.PopularMoviesResult
import com.yernarkt.themoviedb.network.ServiceGenerator
import com.yernarkt.themoviedb.util.API_KEY
import com.yernarkt.themoviedb.util.LANGUAGE
import com.yernarkt.themoviedb.viewHolders.PopularMoviesViewHolder
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class PopularMoviesPresenter(private val context: Context, private val baseView: IBaseView, private val mView: View) {
    private var movieList: ArrayList<PopularMoviesResult>? = null
    private lateinit var adapter: MoviesAdapter<PopularMoviesResult, PopularMoviesViewHolder>

    fun loadPopularMovies(page: Int) {
        movieList = ArrayList()
        baseView.setVisibilityProgressBar(View.VISIBLE)
        val observable = ServiceGenerator.getRetrofitService().getPopularMovieList(
            API_KEY,
            LANGUAGE,
            page
        )

        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<PopularMoviesResponse> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(dataList: PopularMoviesResponse) {
                    baseView.setVisibilityProgressBar(View.GONE)
                    if (dataList.results != null) {
                        movieList!!.addAll(dataList.results!!)
                        adapter = object : MoviesAdapter<PopularMoviesResult, PopularMoviesViewHolder>(
                            R.layout.item_movie,
                            PopularMoviesViewHolder::class.java,
                            PopularMoviesResult::class.java,
                            movieList
                        ) {
                            override fun bindView(holder: PopularMoviesViewHolder, model: PopularMoviesResult, position: Int) {
                                holder.bind(context, model)
                                holder.setClick(dataList, context)
                            }
                        }

                        baseView.setRecyclerViewAdapter(adapter)
                    } else {
                        Snackbar.make(mView, context.getString(R.string.s_there_are_no_elements), Snackbar.LENGTH_SHORT).show()
                    }
                }

                override fun onError(e: Throwable) {
                    baseView.setVisibilityProgressBar(View.GONE)
                    Snackbar.make(mView, context.getString(R.string.s_on_failure_message), Snackbar.LENGTH_SHORT).show()
                    Timber.d(e.localizedMessage)
                }

                override fun onComplete() {

                }
            })
    }
}