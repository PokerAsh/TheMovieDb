package com.yernarkt.themoviedb.ui.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.shimmer.ShimmerFrameLayout
import com.yernarkt.themoviedb.R
import com.yernarkt.themoviedb.util.InternetConnection
import com.yernarkt.themoviedb.view.GenrePresenter
import com.yernarkt.themoviedb.view.IBaseView

class GenresListFragment : Fragment(), IBaseView {
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var mView: View
    private lateinit var presenter: GenrePresenter
    private var shimmerLayout: ShimmerFrameLayout? = null
    private var genreRecyclerView: RecyclerView? = null
    private lateinit var snackBar: Snackbar

    companion object {
        fun newInstance(): GenresListFragment {
            return GenresListFragment()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        appCompatActivity = context as AppCompatActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_genre_list, container, false)
        shimmerLayout = mView.findViewById(R.id.genreListShimmerContainer)
        genreRecyclerView = mView.findViewById(R.id.genresRecyclerView)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPresenter()
    }

    private fun initPresenter() {
        presenter = GenrePresenter(appCompatActivity, this, mView)
    }

    override fun onResume() {
        super.onResume()
        shimmerLayout!!.startShimmer()
        loadGenre()
    }

    private fun loadGenre() {
        snackBar = Snackbar.make(mView, R.string.string_internet_connection_warning, Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction("Понятно") {
            snackBar.dismiss()
        }
        if (InternetConnection.checkConnection(appCompatActivity)) {
            presenter.loadGenreList()
        } else {
            snackBar.show()
        }
    }

    override fun setVisibilityProgressBar(visibility: Int) {
        shimmerLayout!!.visibility = visibility
        when (visibility) {
            View.GONE -> {
                genreRecyclerView!!.visibility = View.VISIBLE
                Handler().postDelayed({ genreRecyclerView!!.scrollToPosition(0) }, 200)
            }
            View.VISIBLE -> {
                shimmerLayout!!.startShimmer()
                genreRecyclerView!!.visibility = View.GONE
            }
        }
    }

    override fun setRecyclerViewAdapter(adapter: RecyclerView.Adapter<*>) {
        val linearLayoutManager = LinearLayoutManager(context)
        genreRecyclerView!!.layoutManager = linearLayoutManager
        genreRecyclerView!!.adapter = adapter
    }

    override fun onPause() {
        super.onPause()
        snackBar.dismiss()
        shimmerLayout!!.stopShimmer()
    }
}