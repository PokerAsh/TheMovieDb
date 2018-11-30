package com.yernarkt.themoviedb.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.yernarkt.themoviedb.R
import com.yernarkt.themoviedb.model.detail.MovieResultDetail
import com.yernarkt.themoviedb.ui.activities.MovieBaseActivity
import com.yernarkt.themoviedb.util.BASE_IMAGE_URL
import com.yernarkt.themoviedb.util.InternetConnection
import com.yernarkt.themoviedb.util.MOVIE_ID
import com.yernarkt.themoviedb.util.MOVIE_TITLE
import com.yernarkt.themoviedb.view.MovieDetailPresenter
import com.yernarkt.themoviedb.view.MovieDetailView
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import timber.log.Timber

class MovieDetailFragment : Fragment(), MovieDetailView {
    private lateinit var appCompatActivity: MovieBaseActivity
    private lateinit var mView: View
    private var presenter: MovieDetailPresenter? = null
    private var movieTitle: String? = null
    private var movieId: String? = null
    private var detailMovieList: RecyclerView? = null
    private var snackBar: Snackbar? = null

    companion object {
        fun newInstance(id: String, title: String): MovieDetailFragment {
            val fragment = MovieDetailFragment()
            val bundle = Bundle()
            bundle.putString(MOVIE_ID, id)
            bundle.putString(MOVIE_TITLE, title)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        appCompatActivity = context as MovieBaseActivity

        val bundle = arguments
        if (bundle != null) {
            movieId = bundle.getString(MOVIE_ID, "")
            movieTitle = bundle.getString(MOVIE_TITLE, "")
            Timber.d(movieId)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_movie_detail, container, false)
        setSupportToolbar()
        return mView
    }


    private fun setSupportToolbar() {
        appCompatActivity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        appCompatActivity.supportActionBar!!.title = movieTitle
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        snackBar = Snackbar.make(mView, R.string.string_internet_connection_warning, Snackbar.LENGTH_INDEFINITE)
        snackBar!!.setAction("Понятно") {
            snackBar!!.dismiss()
        }
        if (InternetConnection.checkConnection(appCompatActivity)) {
            presenter!!.loadMovieById(movieId!!.toInt())
        } else {
            snackBar!!.show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initPresenter()
    }

    private fun initViews() {
        detailMovieList = mView.findViewById(R.id.detailSimilarMoviesList)
    }

    private fun initPresenter() {
        presenter = MovieDetailPresenter(appCompatActivity, this, mView)
    }

    override fun setVisibilityProgressBar(visibility: Int) {
        detailProgressBar!!.visibility = visibility
        when (visibility) {
            View.GONE -> {
                detailMovieList!!.visibility = View.VISIBLE
                Handler().postDelayed({ detailMovieList!!.scrollToPosition(0) }, 200)
            }
            View.VISIBLE -> {
                detailMovieList!!.visibility = View.GONE
            }
        }
    }

    override fun setRecyclerViewAdapter(adapter: RecyclerView.Adapter<*>) {
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        detailMovieList!!.layoutManager = linearLayoutManager
        detailMovieList!!.adapter = adapter
    }

    override fun setMovieDetail(data: MovieResultDetail) {
        val options = RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .error(R.drawable.ic_no_image)
            .priority(Priority.HIGH)

        if (data.posterPath != null)
            Glide.with(appCompatActivity)
                .asBitmap()
                .load(String.format("%s%s", BASE_IMAGE_URL, data.posterPath))
                .apply(options)
                .into(detailImageView)

        detailDescription!!.text = if (!data.overview!!.isEmpty())data.overview!! else getString(R.string.s_no_overview)
    }

    override fun setMovieCredit(cast: String) {
        detailActors!!.text = if(!cast.isEmpty())cast else getString(R.string.s_no_credits)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                appCompatActivity.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}