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
import com.yernarkt.themoviedb.util.InternetConnection
import com.yernarkt.themoviedb.view.IBaseView
import com.yernarkt.themoviedb.view.PopularMoviesPresenter

class MoviesPopularFragment : Fragment(), IBaseView {
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var mView: View
    private lateinit var presenter: PopularMoviesPresenter
    private var popularProgressBar: ProgressBar? = null
    private var popularRecyclerView: RecyclerView? = null

    private var page = PAGE_NUMBER

    companion object {
        private const val PAGE_NUMBER: Int = 1

        fun newInstance(): MoviesPopularFragment {
            return MoviesPopularFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appCompatActivity = context as AppCompatActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_movies_popular, container, false)
        popularProgressBar = mView.findViewById(R.id.popularProgressBar)
        popularRecyclerView = mView.findViewById(R.id.popularRecyclerView)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPresenter()
    }

    override fun onResume() {
        super.onResume()
        loadPopularMovies(page)
    }

    private fun loadPopularMovies(page: Int) {
        if (InternetConnection.checkConnection(appCompatActivity)) {
            presenter.loadPopularMovies(page)
        } else {
            Snackbar.make(mView, R.string.string_internet_connection_warning, Snackbar.LENGTH_INDEFINITE).show()
        }
    }

    private fun initPresenter() {
        presenter = PopularMoviesPresenter(appCompatActivity, this, mView)
    }

    override fun setVisibilityProgressBar(visibility: Int) {
        when (visibility) {
            View.GONE -> {
                popularProgressBar!!.visibility = View.GONE
                popularRecyclerView!!.visibility = View.VISIBLE
                Handler().postDelayed({ popularRecyclerView!!.scrollToPosition(0) }, 200)
            }
            View.VISIBLE -> {
                popularProgressBar!!.visibility = View.VISIBLE
                popularRecyclerView!!.visibility = View.GONE
            }
        }
    }

    override fun setRecyclerViewAdapter(adapter: RecyclerView.Adapter<*>) {
        val linearLayoutManager = GridLayoutManager(context, 2)
        popularRecyclerView!!.layoutManager = linearLayoutManager
        popularRecyclerView!!.adapter = adapter
    }
}