package com.yernarkt.themoviedb.view

import android.app.PendingIntent.getActivity
import android.content.Context
import android.support.design.widget.Snackbar
import android.support.transition.*
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.yernarkt.themoviedb.R
import com.yernarkt.themoviedb.adapter.MoviesAdapter
import com.yernarkt.themoviedb.model.MoviesResponse
import com.yernarkt.themoviedb.model.MoviesResult
import com.yernarkt.themoviedb.model.upcoming.UpcomingMoviesResponse
import com.yernarkt.themoviedb.network.ServiceGenerator
import com.yernarkt.themoviedb.ui.fragment.MovieDetailFragment
import com.yernarkt.themoviedb.ui.fragment.MoviesListFragment
import com.yernarkt.themoviedb.util.*
import com.yernarkt.themoviedb.viewHolders.MoviesViewHolder
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


class MoviesListPresenter(
    private val context: Context,
    private val baseView: IBaseView,
    private val mView: View,
    private val moviesListFragment: MoviesListFragment
) {
    private var movieList: ArrayList<MoviesResult>? = null
    private var adapter: MoviesAdapter<MoviesResult, MoviesViewHolder>? = null

    fun loadMovies(
        page: Int, movieType: String, genreId: String, startYear: String,
        endYear: String
    ) {
        movieList = ArrayList()
        baseView.setVisibilityProgressBar(View.VISIBLE)
        val observable = when (movieType) {
            "Popular" -> generateMovieResponse(page)
            "Genre" -> generateByGenreMovieResponse(genreId, page)
            "Sorted" -> generateSortedMovieResponse(startYear, endYear, genreId, page)
            else -> {
                generateUpcomingMoviesResponse(page)
            }
        }
        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<MoviesResponse> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(dataList: MoviesResponse) {
                    baseView.setVisibilityProgressBar(View.GONE)
                    if (dataList.results != null) {
                        movieList!!.addAll(dataList.results!!)
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
                                            moviesListFragment.exitTransition = Fade()
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

                        baseView.setRecyclerViewAdapter(adapter!!)
                    } else {
                        Snackbar.make(mView, context.getString(R.string.s_there_are_no_elements), Snackbar.LENGTH_SHORT)
                            .show()
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

    private fun generateUpcomingMoviesResponse(page: Int): Observable<UpcomingMoviesResponse> {
        return ServiceGenerator.getRetrofitService().getUpcomingMovieList(
            API_KEY,
            LANGUAGE,
            page
        )
    }

    private fun generateMovieResponse(page: Int): Observable<MoviesResponse> {
        return ServiceGenerator.getRetrofitService().getPopularMovieList(
            API_KEY,
            LANGUAGE,
            page
        )
    }

    private fun generateByGenreMovieResponse(genreId: String, page: Int): Observable<MoviesResponse> {
        return ServiceGenerator.getRetrofitService().getMoviesByGenre(
            API_KEY,
            LANGUAGE,
            SORT_BY_POPULARITY_DESC,
            page,
            genreId
        )
    }

    private fun generateSortedMovieResponse(
        startYear: String,
        endYear: String,
        genreId: String,
        page: Int
    ): Observable<MoviesResponse> {
        return ServiceGenerator.getRetrofitService().getSortedMovies(
            API_KEY,
            LANGUAGE,
            SORT_BY_PRIMARY_RELEASE_DATE_DESC,
            page,
            getAdditionalFieldForQueryResponse(
                startYear,
                endYear,
                genreId
            )
        )
    }

    private fun getAdditionalFieldForQueryResponse(
        startYear: String,
        endYear: String,
        genreId: String
    ): HashMap<String, String> {
        val map = HashMap<String, String>()
        map["primary_release_date.gte"] = startYear
        map["primary_release_date.lte"] = endYear
        map["with_genres"] = genreId
        return map
    }
}