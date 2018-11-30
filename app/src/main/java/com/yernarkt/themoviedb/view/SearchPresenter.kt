package com.yernarkt.themoviedb.view

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.yernarkt.themoviedb.R
import com.yernarkt.themoviedb.adapter.MoviesAdapter
import com.yernarkt.themoviedb.model.MoviesResponse
import com.yernarkt.themoviedb.model.MoviesResult
import com.yernarkt.themoviedb.network.ServiceGenerator
import com.yernarkt.themoviedb.ui.activities.SearchActivity
import com.yernarkt.themoviedb.util.API_KEY
import com.yernarkt.themoviedb.util.LANGUAGE
import com.yernarkt.themoviedb.util.OnRecyclerViewItemClickListener
import com.yernarkt.themoviedb.viewHolders.MoviesViewHolder
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


class SearchPresenter(val context: Context, val iBaseView: IBaseView) {
    private var adapter: MoviesAdapter<MoviesResult, MoviesViewHolder>? = null
    private var movieList: ArrayList<MoviesResult>? = null

    fun searchEngine(s: CharSequence?) {
        iBaseView.setVisibilityProgressBar(View.VISIBLE)
        val observable = ServiceGenerator.getRetrofitService().searchMovie(
            API_KEY,
            LANGUAGE,
            s,
            1
        )

        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .distinct()
            .subscribe(object : Observer<MoviesResponse> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(moviesResponse: MoviesResponse) {
                    movieList = ArrayList()
                    iBaseView.setVisibilityProgressBar(View.GONE)
                    if (moviesResponse.results != null) {
                        movieList!!.addAll(moviesResponse.results!!)
                        adapter = object : MoviesAdapter<MoviesResult, MoviesViewHolder>(
                            R.layout.item_movie,
                            MoviesViewHolder::class.java,
                            MoviesResult::class.java,
                            movieList
                        ) {
                            override fun bindView(
                                holder: MoviesViewHolder,
                                model: MoviesResult,
                                position: Int
                            ) {
                                holder.bind(context, model)
                                holder.setClick(object : OnRecyclerViewItemClickListener {
                                    override fun onItemClick(position: Int) {
                                        val activity = context as SearchActivity
                                        val intent = Intent()
                                        intent.putExtra("MovieId", movieList!![position].id.toString())
                                        intent.putExtra("MovieTitle", movieList!![position].title!!)
                                        activity.setResult(RESULT_OK, intent)
                                        activity.finish()
                                    }
                                })
                            }
                        }
                        iBaseView.setRecyclerViewAdapter(adapter!!)
                    } else {
                        Toast.makeText(context, context.getString(R.string.s_there_are_no_elements), Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onError(e: Throwable) {
                    iBaseView.setVisibilityProgressBar(View.GONE)
                    Toast.makeText(context, context.getString(R.string.s_on_failure_message), Toast.LENGTH_SHORT).show()
                    Timber.d(e.localizedMessage)
                }

                override fun onComplete() {

                }
            })
    }

    fun clearAdapter() {
        if (adapter != null) {
            adapter!!.clear()
        }
    }
}