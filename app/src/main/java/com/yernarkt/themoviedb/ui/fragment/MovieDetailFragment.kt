package com.yernarkt.themoviedb.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yernarkt.themoviedb.R
import com.yernarkt.themoviedb.ui.activities.MovieBaseActivity
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
        initViews()
        setSupportToolbar()
        return mView
    }

    private fun setSupportToolbar() {
        try {
            val f = appCompatActivity.supportActionBar!!::class.java.getDeclaredField("mTitleTextView")
            f.isAccessible = true
            val titleTextView = f.get(appCompatActivity.supportActionBar!!) as TextView
            titleTextView.ellipsize = TextUtils.TruncateAt.MARQUEE
            titleTextView.isFocusable = true
            titleTextView.isFocusableInTouchMode = true
            titleTextView.requestFocus()
            titleTextView.setSingleLine(true)
            titleTextView.isSelected = true
            titleTextView.marqueeRepeatLimit = -1
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        appCompatActivity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        appCompatActivity.supportActionBar!!.title = movieTitle
    }

    private fun initViews() {
//        detailImageView
//        detailActors
//        detailDescription
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPresenter()
    }

    private fun initPresenter() {
        presenter = MovieDetailPresenter(appCompatActivity, this)
    }

    override fun setVisibilityProgressBar(visibility: Int) {
        detailProgressBar.visibility = visibility
    }

    override fun setViews() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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