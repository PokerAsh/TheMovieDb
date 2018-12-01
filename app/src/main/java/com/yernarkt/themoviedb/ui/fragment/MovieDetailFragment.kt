package com.yernarkt.themoviedb.ui.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.facebook.shimmer.ShimmerFrameLayout
import com.yernarkt.themoviedb.R
import com.yernarkt.themoviedb.ui.activities.MovieBaseActivity
import com.yernarkt.themoviedb.util.*
import com.yernarkt.themoviedb.view.MovieDetailPresenter
import com.yernarkt.themoviedb.view.MovieDetailView

class MovieDetailFragment : Fragment(), MovieDetailView {
    private lateinit var appCompatActivity: MovieBaseActivity
    private lateinit var mView: View
    private var detailImageShimmer: ShimmerFrameLayout? = null
    private var detailCreditShimmer: ShimmerFrameLayout? = null
    private var detailOverviewShimmer: ShimmerFrameLayout? = null
    private var detailRecyclerViewShimmer: ShimmerFrameLayout? = null


    private var presenter: MovieDetailPresenter? = null
    private var detailMovieList: RecyclerView? = null
    private var detailImageView: ImageView? = null
    private var detailView: View? = null
    private var detailTitle: TextView? = null
    private var detailActors: AppCompatTextView? = null
    private var detailDescription: AppCompatTextView? = null
    private var snackBar: Snackbar? = null

    private var movieTitle: String? = null
    private var movieId: String? = null
    private var moviePoster: String? = null
    private var movieOverview: String? = null

    companion object {
        fun newInstance(id: String, title: String, poster: String, movieOverview: String): MovieDetailFragment {
            val fragment = MovieDetailFragment()
            val bundle = Bundle()
            bundle.putString(MOVIE_ID, id)
            bundle.putString(MOVIE_TITLE, title)
            bundle.putString(MOVIE_POSTER, poster)
            bundle.putString(MOVIE_OVERVIEW, movieOverview)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        appCompatActivity = context as MovieBaseActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bundle = arguments
        if (bundle != null) {
            movieId = bundle.getString(MOVIE_ID, "")
            movieTitle = bundle.getString(MOVIE_TITLE, "")
            moviePoster = bundle.getString(MOVIE_POSTER, "")
            movieOverview = bundle.getString(MOVIE_OVERVIEW, "")
        }

        mView = inflater.inflate(R.layout.fragment_movie_detail, container, false)
        setSupportToolbar()
        initViews()
        return mView
    }


    private fun setSupportToolbar() {
        appCompatActivity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        appCompatActivity.supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_action_back)
    }

    override fun onResume() {
        super.onResume()
        shimmerTrigger(true)
        loadData()
    }

    private fun shimmerTrigger(isToStart: Boolean) {
        if (isToStart) {
            detailImageShimmer!!.startShimmer()
            detailCreditShimmer!!.startShimmer()
            detailOverviewShimmer!!.startShimmer()
            detailRecyclerViewShimmer!!.startShimmer()
        } else {
            detailImageShimmer!!.stopShimmer()
            detailCreditShimmer!!.stopShimmer()
            detailOverviewShimmer!!.stopShimmer()
            detailRecyclerViewShimmer!!.stopShimmer()
        }
    }

    private fun loadData() {
        snackBar = Snackbar.make(mView, R.string.string_internet_connection_warning, Snackbar.LENGTH_INDEFINITE)
        snackBar!!.setAction("Понятно") {
            snackBar!!.dismiss()
        }
        if (InternetConnection.checkConnection(appCompatActivity)) {
            presenter!!.loadMovieCredits(movieId!!.toInt())
        } else {
            snackBar!!.show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPresenter()
    }

    private fun initViews() {
        detailMovieList = mView.findViewById(R.id.detailSimilarMoviesList)
        detailImageView = mView.findViewById(R.id.detailImageView)
        detailActors = mView.findViewById(R.id.detailActors)
        detailDescription = mView.findViewById(R.id.detailDescription)
        detailView = mView.findViewById(R.id.detailViewBackground)
        detailTitle = mView.findViewById(R.id.detailNameText)

        detailImageShimmer = mView.findViewById(R.id.detailImageViewShimmerContainer)
        detailCreditShimmer = mView.findViewById(R.id.detailCreditsShimmerContainer)
        detailOverviewShimmer = mView.findViewById(R.id.detailOverViewShimmerContainer)
        detailRecyclerViewShimmer = mView.findViewById(R.id.listShimmerContainer)

        setTransitionAnimation()
    }

    private fun setTransitionAnimation() {
        val options = RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .error(R.drawable.ic_no_image)
            .priority(Priority.HIGH)

        if (moviePoster != null)
            Glide.with(appCompatActivity)
                .asBitmap()
                .load(String.format("%s%s", BASE_IMAGE_URL, moviePoster))
                .apply(options)
                .into(object : BitmapImageViewTarget(detailImageView) {
                    override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                        super.onResourceReady(bitmap, transition)
                        Palette.from(bitmap)
                            .generate { palette -> setBackgroundColor(context as AppCompatActivity, palette!!) }
                    }
                })
        detailTitle!!.text = movieTitle

        detailDescription!!.text =
                if (!movieOverview!!.isEmpty()) movieOverview else getString(R.string.s_no_overview)
    }

    override fun onPause() {
        shimmerTrigger(false)
        snackBar!!.dismiss()
        super.onPause()
    }

    private fun initPresenter() {
        presenter = MovieDetailPresenter(appCompatActivity, this, mView, this@MovieDetailFragment)
    }

    override fun setVisibilityProgressBar(visibility: Int) {
        detailImageShimmer!!.visibility = visibility
        detailOverviewShimmer!!.visibility = visibility
        detailCreditShimmer!!.visibility = visibility
        detailRecyclerViewShimmer!!.visibility = visibility

        when (visibility) {
            View.GONE -> {
                detailMovieList!!.visibility = View.VISIBLE
                Handler().postDelayed({ detailMovieList!!.scrollToPosition(0) }, 200)
            }
            View.VISIBLE -> {
                shimmerTrigger(true)
                detailMovieList!!.visibility = View.GONE
            }
        }
    }

    override fun setRecyclerViewAdapter(adapter: RecyclerView.Adapter<*>) {
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        detailMovieList!!.layoutManager = linearLayoutManager
        detailMovieList!!.adapter = adapter
    }

//    override fun setMovieDetail(data: MovieResultDetail) {
//        val options = RequestOptions()
//            .centerCrop()
//            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//            .error(R.drawable.ic_no_image)
//            .priority(Priority.HIGH)
//
//        if (data.posterPath != null)
//            Glide.with(appCompatActivity)
//                .asBitmap()
//                .load(String.format("%s%s", BASE_IMAGE_URL, data.posterPath))
//                .apply(options)
//                .into(object : BitmapImageViewTarget(detailImageView) {
//                    override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
//                        super.onResourceReady(bitmap, transition)
//                        Palette.from(bitmap)
//                            .generate { palette -> setBackgroundColor(context as AppCompatActivity, palette!!) }
//                    }
//                })
//
//        detailTitle!!.text = movieTitle
//
//        detailDescription!!.text =
//                if (!data.overview!!.isEmpty()) data.overview!! else getString(R.string.s_no_overview)
//    }

    private fun setBackgroundColor(context: AppCompatActivity, palette: Palette) {
        detailView!!.setBackgroundColor(
            palette.getVibrantColor(
                ContextCompat.getColor(context, R.color.black_translucent_60)
            )
        )
    }

    override fun setMovieCredit(cast: String) {
        detailActors!!.text = if (!cast.isEmpty()) cast else getString(R.string.s_no_credits)
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