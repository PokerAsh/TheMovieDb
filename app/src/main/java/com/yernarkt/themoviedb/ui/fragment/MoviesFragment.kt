package com.yernarkt.themoviedb.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.yernarkt.themoviedb.R
import com.yernarkt.themoviedb.model.MoviesResult
import com.yernarkt.themoviedb.util.InternetConnection
import com.yernarkt.themoviedb.util.MOVIE_TYPE
import com.yernarkt.themoviedb.view.IBaseView
import com.yernarkt.themoviedb.view.MoviesPresenter

class MoviesFragment : Fragment(), IBaseView {
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var mView: View
    private lateinit var presenter: MoviesPresenter
    private var moviesProgressBar: ProgressBar? = null
    private var moviesRecyclerView: RecyclerView? = null

    private lateinit var movieList: ArrayList<MoviesResult>
    private var snackBar: Snackbar? = null
    private var page = PAGE_NUMBER
    private lateinit var movieType: String

    companion object {
        private const val PAGE_NUMBER: Int = 1

        fun newInstance(movieType: String): MoviesFragment {
            val fragment = MoviesFragment()
            val bundle = Bundle()
            bundle.putString(MOVIE_TYPE, movieType)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appCompatActivity = context as AppCompatActivity

        val bundle = arguments
        if (bundle != null) {
            movieType = bundle.getString(MOVIE_TYPE, "Popular")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_movies_popular, container, false)
        movieList = ArrayList()
        moviesProgressBar = mView.findViewById(R.id.moviesProgressBar)
        moviesRecyclerView = mView.findViewById(R.id.moviesRecyclerView)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTitle()
        initPresenter()
    }

    private fun setupTitle() {
        when (movieType) {
            "Popular" -> appCompatActivity.supportActionBar!!.title = resources.getString(R.string.s_movies)
            "Upcoming" -> appCompatActivity.supportActionBar!!.title = resources.getString(R.string.s_soon)
        }
    }

    override fun onResume() {
        super.onResume()
        loadMovies(page)
    }

    private fun loadMovies(page: Int) {
        snackBar = Snackbar.make(mView, R.string.string_internet_connection_warning, Snackbar.LENGTH_INDEFINITE)
        snackBar!!.setAction("Понятно") {
            snackBar!!.dismiss()
        }
        if (InternetConnection.checkConnection(appCompatActivity)) {
            loadMovieType(page, movieType)
        } else {
            snackBar!!.show()
        }
    }

    private fun loadMovieType(page: Int, movieType: String) {
        presenter.loadMovies(page, movieType)
    }

    private fun initPresenter() {
        presenter = MoviesPresenter(appCompatActivity, this, mView, movieList)
    }

    override fun setVisibilityProgressBar(visibility: Int) {
        when (visibility) {
            View.GONE -> {
                moviesProgressBar!!.visibility = View.GONE
                moviesRecyclerView!!.visibility = View.VISIBLE
                Handler().postDelayed({ moviesRecyclerView!!.scrollToPosition(0) }, 200)
            }
            View.VISIBLE -> {
                moviesProgressBar!!.visibility = View.VISIBLE
                moviesRecyclerView!!.visibility = View.GONE
            }
        }
    }

    override fun setRecyclerViewAdapter(adapter: RecyclerView.Adapter<*>) {
        val linearLayoutManager = GridLayoutManager(context, 2)
        moviesRecyclerView!!.layoutManager = linearLayoutManager
        moviesRecyclerView!!.adapter = adapter
    }

    override fun onPause() {
        super.onPause()
        snackBar!!.dismiss()
    }
}