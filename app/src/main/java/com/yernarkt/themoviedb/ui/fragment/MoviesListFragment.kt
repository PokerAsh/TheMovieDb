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
import android.widget.ProgressBar
import com.yernarkt.themoviedb.R
import com.yernarkt.themoviedb.util.GENRE_ID
import com.yernarkt.themoviedb.util.InternetConnection
import com.yernarkt.themoviedb.util.MOVIE_TYPE
import com.yernarkt.themoviedb.view.IBaseView
import com.yernarkt.themoviedb.view.MoviesPresenter

class MoviesListFragment : Fragment(), IBaseView {
    private lateinit var appCompatBaseActivity: AppCompatActivity
    private lateinit var mView: View
    private lateinit var presenter: MoviesPresenter
    private var moviesProgressBar: ProgressBar? = null
    private var moviesRecyclerView: RecyclerView? = null

    private var snackBar: Snackbar? = null
    private var page = PAGE_NUMBER
    private lateinit var movieType: String
    private lateinit var genreId: String

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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        appCompatBaseActivity = context as AppCompatActivity

        val bundle = arguments
        if (bundle != null) {
            movieType = bundle.getString(MOVIE_TYPE, "Popular")
            genreId = bundle.getString(GENRE_ID, "0")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_list, container, false)
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
        appCompatBaseActivity.supportActionBar!!.title = resources.getString(R.string.s_movies)
        if (movieType == "Genre") {
            appCompatBaseActivity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
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
        if (InternetConnection.checkConnection(appCompatBaseActivity)) {
            loadMovieType(page, movieType)
        } else {
            snackBar!!.show()
        }
    }

    private fun loadMovieType(page: Int, movieType: String) {
        presenter.loadMovies(page, movieType, genreId)
    }

    private fun initPresenter() {
        presenter = MoviesPresenter(appCompatBaseActivity, this, mView)
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