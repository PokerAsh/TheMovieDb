package com.yernarkt.themoviedb.view

import android.content.Context
import android.support.design.widget.Snackbar
import android.support.transition.*
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.yernarkt.themoviedb.R
import com.yernarkt.themoviedb.adapter.MoviesAdapter
import com.yernarkt.themoviedb.model.detail.Cast
import com.yernarkt.themoviedb.model.detail.MovieCreditsResult
import com.yernarkt.themoviedb.model.detail.SimilarMoviesResponse
import com.yernarkt.themoviedb.model.detail.SimilarMoviesResult
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
    private val mView: View,
    private val moviesDetailFragment: MovieDetailFragment
) {
    private var movieList: ArrayList<SimilarMoviesResult>? = null
    private var adapter: MoviesAdapter<SimilarMoviesResult, MoviesViewHolder>? = null

//    fun loadMovieById(id: Int) {
//        detailView.setVisibilityProgressBar(View.VISIBLE)
//        val observable = ServiceGenerator.getRetrofitService().getMovieById(
//            id,
//            API_KEY,
//            LANGUAGE
//        )
//
//        observable
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : Observer<MovieResultDetail> {
//                override fun onSubscribe(d: Disposable) {
//
//                }
//
//                override fun onNext(data: MovieResultDetail) {
//                    val result: MovieResultDetail? = data
//                    if (result != null)
//                        detailView.setMovieDetail(data)
//                    else
//                        Snackbar.make(
//                            mView,
//                            context.getString(R.string.s_something_went_wrong),
//                            Snackbar.LENGTH_LONG
//                        ).show()
//                }
//
//                override fun onError(e: Throwable) {
//                    detailView.setVisibilityProgressBar(View.GONE)
//                    Snackbar.make(mView, context.getString(R.string.s_on_failure_message), Snackbar.LENGTH_SHORT).show()
//                    Timber.d(e.localizedMessage)
//                }
//
//                override fun onComplete() {
//                    loadMovieCredits(id)
//                }
//            })
//    }

    fun loadMovieCredits(id: Int) {
        detailView.setVisibilityProgressBar(View.VISIBLE)
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
                    val result: MovieCreditsResult? = data
                    if (result != null)
                        detailView.setMovieCredit(concatStringsWSep(result.cast!!))
                    else
                        Snackbar.make(
                            mView,
                            context.getString(R.string.s_something_went_wrong),
                            Snackbar.LENGTH_LONG
                        ).show()
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
                                            val detailFragment = MovieDetailFragment.newInstance(
                                                movieList!![position].id.toString(),
                                                movieList!![position].title!!,
                                                movieList!![position].posterPath!!,
                                                movieList!![position].overview!!
                                            )

                                            val set = TransitionSet()
                                            set.ordering = TransitionSet.ORDERING_TOGETHER
                                            set.addTransition(ChangeBounds()).addTransition(ChangeTransform())
                                                .addTransition(ChangeImageTransform())
                                            detailFragment.sharedElementEnterTransition = set
                                            detailFragment.enterTransition = Fade()
                                            moviesDetailFragment.exitTransition = Fade()
                                            detailFragment.sharedElementReturnTransition = set

                                            val activity = context as AppCompatActivity
                                            activity.supportFragmentManager
                                                .beginTransaction()
                                                .addSharedElement(
                                                    holder.moviesPoster,
                                                    holder.moviesPoster.transitionName
                                                )
                                                .addSharedElement(holder.moviesName, holder.moviesName.transitionName)
                                                .addSharedElement(holder.moviesView, holder.moviesView.transitionName)
                                                .replace(
                                                    R.id.activity_container,
                                                    detailFragment
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