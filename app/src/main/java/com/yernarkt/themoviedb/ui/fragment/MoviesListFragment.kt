package com.yernarkt.themoviedb.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.facebook.shimmer.ShimmerFrameLayout
import com.yernarkt.themoviedb.R
import com.yernarkt.themoviedb.util.*
import com.yernarkt.themoviedb.view.IBaseView
import com.yernarkt.themoviedb.view.MoviesListPresenter

class MoviesListFragment : Fragment(), IBaseView {
    private lateinit var appCompatBaseActivity: AppCompatActivity
    private lateinit var mView: View
    private lateinit var presenter: MoviesListPresenter
    private var shimmerLayout: ShimmerFrameLayout? = null
    private var moviesRecyclerView: RecyclerView? = null

    private var snackBar: Snackbar? = null
    private var page = PAGE_NUMBER
    private var movieType: String? = null
    private var genreId: String? = null
    private var startYear: String? = null
    private var endYear: String? = null

    companion object {
        private const val PAGE_NUMBER: Int = 1

        fun newInstance(movieType: String): MoviesListFragment {
            val fragment = MoviesListFragment()
            val bundle = Bundle()
            bundle.putString(MOVIE_TYPE, movieType)
            fragment.arguments = bundle
            return fragment
        }

        fun newInstance(movieType: String, genreId: String?): MoviesListFragment {
            val fragment = MoviesListFragment()
            val bundle = Bundle()
            bundle.putString(MOVIE_TYPE, movieType)
            bundle.putString(GENRE_ID, genreId)
            fragment.arguments = bundle
            return fragment
        }

        fun newInstance(movieType: String, genreId: String?, startYear: String, endYear: String): MoviesListFragment {
            val fragment = MoviesListFragment()
            val bundle = Bundle()
            bundle.putString(MOVIE_TYPE, movieType)
            bundle.putString(GENRE_ID, genreId)
            bundle.putString(START_YEAR, startYear)
            bundle.putString(END_YEAR, endYear)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        appCompatBaseActivity = context as AppCompatActivity

        val bundle = arguments
        if (bundle != null) {
            movieType = bundle.getString(MOVIE_TYPE, "Popular")
            genreId = bundle.getString(GENRE_ID, "0")
            startYear = bundle.getString(START_YEAR, "2018-01-01")
            endYear = bundle.getString(END_YEAR, "2019-01-01")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_movie_list, container, false)
        shimmerLayout = mView.findViewById(R.id.movieListShimmerContainer)
        moviesRecyclerView = mView.findViewById(R.id.moviesRecyclerView)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTitle()
        initPresenter()
        loadMovies()
    }

    private fun setupTitle() {
        if (movieType == "Genre" || movieType == "Sorted") {
            appCompatBaseActivity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onResume() {
        super.onResume()
        shimmerLayout!!.startShimmer()
    }

    override fun onPause() {
        shimmerLayout!!.stopShimmer()
        snackBar!!.dismiss()
        super.onPause()
    }

    private fun loadMovies() {
        snackBar = Snackbar.make(mView, R.string.string_internet_connection_warning, Snackbar.LENGTH_INDEFINITE)
        snackBar!!.setAction("Понятно") {
            snackBar!!.dismiss()
        }
        if (InternetConnection.checkConnection(appCompatBaseActivity)) {
            loadMovieType()
        } else {
            snackBar!!.show()
        }
    }

    private fun loadMovieType() {
        presenter.loadMovies(page, movieType!!, genreId!!, startYear!!, endYear!!)
    }

    private fun initPresenter() {
        presenter = MoviesListPresenter(appCompatBaseActivity, this, mView, this@MoviesListFragment)
    }

    override fun setVisibilityProgressBar(visibility: Int) {
        shimmerLayout!!.visibility = visibility
        when (visibility) {
            View.GONE -> {
                shimmerLayout!!.stopShimmer()
                moviesRecyclerView!!.visibility = View.VISIBLE
                Handler().postDelayed({ moviesRecyclerView!!.scrollToPosition(0) }, 200)
            }
            View.VISIBLE -> {
                shimmerLayout!!.startShimmer()
                moviesRecyclerView!!.visibility = View.GONE
            }
        }
    }

    override fun setRecyclerViewAdapter(adapter: RecyclerView.Adapter<*>) {
        val linearLayoutManager = GridLayoutManager(context, 2)
        moviesRecyclerView!!.layoutManager = linearLayoutManager
        moviesRecyclerView!!.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                appCompatBaseActivity.onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}