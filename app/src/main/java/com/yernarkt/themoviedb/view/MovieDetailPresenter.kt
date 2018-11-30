package com.yernarkt.themoviedb.view

import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.yernarkt.themoviedb.R
import com.yernarkt.themoviedb.adapter.MoviesAdapter
import com.yernarkt.themoviedb.model.detail.*
import com.yernarkt.themoviedb.network.ServiceGenerator
import com.yernarkt.themoviedb.ui.fragment.MovieDetailFragment
import com.yernarkt.themoviedb.util.API_KEY
import com.yernarkt.themoviedb.util.LANGUAGE
import com.yernarkt.themoviedb.util.OnRecyclerViewItemClickListener
import com.yernarkt.themoviedb.viewHolders.MoviesViewHolder
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MovieDetailPresenter(
    private val context: Context,
    private val detailView: MovieDetailView,
    private val mView: View
) {
    private var movieList: ArrayList<SimilarMoviesResult>? = null
    private var adapter: MoviesAdapter<SimilarMoviesResult, MoviesViewHolder>? = null

    fun loadMovieById(id: Int) {
        detailView.setVisibilityProgressBar(View.VISIBLE)
        val observable = ServiceGenerator.getRetrofitService().getMovieById(
            id,
            API_KEY,
            LANGUAGE
        )

        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<MovieResultDetail> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(data: MovieResultDetail) {
                    detailView.setMovieDetail(data)
                }

                override fun onError(e: Throwable) {
                    detailView.setVisibilityProgressBar(View.GONE)
                    Snackbar.make(mView, context.getString(R.string.s_on_failure_message), Snackbar.LENGTH_SHORT).show()
                    Timber.d(e.localizedMessage)
                }

                override fun onComplete() {
                    loadMovieCredits(id)
                }
            })
    }

    private fun loadMovieCredits(id: Int) {
        val observable = ServiceGenerator.getRetrofitService().getMovieCredits(
            id,
            API_KEY
        )

        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<MovieCreditsResult> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(data: MovieCreditsResult) {
                    detailView.setMovieCredit(concatStringsWSep(data.cast!!))
                }

                override fun onError(e: Throwable) {
                    detailView.setVisibilityProgressBar(View.GONE)
                    Snackbar.make(mView, context.getString(R.string.s_on_failure_message), Snackbar.LENGTH_SHORT).show()
                    Timber.d(e.localizedMessage)
                }

                override fun onComplete() {
                    loadSimilarMovies(id)
                }
            })
    }

    private fun concatStringsWSep(strings: List<Cast>): String {
        val sb = StringBuilder()
        var sep = ""
        for (s in strings) {
            sb.append(sep).append(s.name)
            sep = ", "
        }
        return sb.toString()
    }

    private fun loadSimilarMovies(id: Int) {
        movieList = ArrayList()
        val observable = ServiceGenerator.getRetrofitService().getSimilarMovies(
            id,
            API_KEY,
            LANGUAGE
        )

        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<SimilarMoviesResponse> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(dataList: SimilarMoviesResponse) {
                    detailView.setVisibilityProgressBar(View.GONE)
                    if (dataList.results != null) {
                        movieList!!.addAll(dataList.results!!)
                        adapter = object : MoviesAdapter<SimilarMoviesResult, MoviesViewHolder>(
                            R.layout.item_similar_movie,
                            MoviesViewHolder::class.java,
                            SimilarMoviesResult::class.java,
                            movieList
                        ) {
                            override fun bindView(
                                holder: MoviesViewHolder,
                                model: SimilarMoviesResult,
                                position: Int
                            ) {
                                holder.bind(context, model)
                                holder.setClick(object : OnRecyclerViewItemClickListener {
                                    override fun onItemClick(position: Int) {
                                        if (position != RecyclerView.NO_POSITION) {
                                            val activity = context as AppCompatActivity
                                            activity.supportFragmentManager
                                                .beginTransaction()
                                                .replace(
                                                    R.id.activity_container,
                                                    MovieDetailFragment.newInstance(
                                                        movieList!![position].id.toString(),
                                                        movieList!![position].title!!
                                                    )
                                                )
                                                .addToBackStack("movie_list")
                                                .commit()
                                        }
                                    }
                                })
                            }
                        }

                        detailView.setRecyclerViewAdapter(adapter!!)
                    } else {
                        Snackbar.make(mView, context.getString(R.string.s_there_are_no_elements), Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onError(e: Throwable) {
                    detailView.setVisibilityProgressBar(View.GONE)
                    Snackbar.make(mView, context.getString(R.string.s_on_failure_message), Snackbar.LENGTH_SHORT).show()
                    Timber.d(e.localizedMessage)
                }

                override fun onComplete() {

                }
            })
    }
}