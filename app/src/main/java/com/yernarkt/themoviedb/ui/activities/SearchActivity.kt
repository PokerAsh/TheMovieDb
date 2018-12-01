package com.yernarkt.themoviedb.ui.activities

import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.transition.Transition
import android.transition.TransitionManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.widget.EditText
import com.facebook.shimmer.ShimmerFrameLayout
import com.yernarkt.themoviedb.R
import com.yernarkt.themoviedb.util.custom.CustomSearchBar
import com.yernarkt.themoviedb.util.transition.FadeInTransition
import com.yernarkt.themoviedb.util.transition.FadeOutTransition
import com.yernarkt.themoviedb.util.transition.SimpleTransitionListener
import com.yernarkt.themoviedb.view.IBaseView
import com.yernarkt.themoviedb.view.SearchPresenter
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.Disposable
import java.io.IOException
import java.util.concurrent.TimeUnit

class SearchActivity : SomeUtilityActivity(), IBaseView {
    private var searchbar: CustomSearchBar? = null
    private var searchMoviesText: EditText? = null
    private var searchShimmer: ShimmerFrameLayout? = null
    private var searchRecyclerView: RecyclerView? = null
    private var searchPresenter: SearchPresenter? = null
    private var observable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initViews()
        setSupportActionBar(searchbar)
        initPresenter()
        searchMovies()
        if (isFirstTimeRunning(savedInstanceState)) {
            searchbar!!.hideContent()

            val viewTreeObserver = searchbar!!.viewTreeObserver
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    searchbar!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    showSearch()
                }

                private fun showSearch() {
                    TransitionManager.beginDelayedTransition(searchbar, FadeInTransition.createTransition())
                    searchbar!!.showContent()
                }
            })
        }
    }

    private fun searchMovies() {
        observable = Observable.create(ObservableOnSubscribe<String> { subscriber ->
            searchMoviesText!!.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s != null) {
                        subscriber.onNext(s.toString())
                    } else {
                        subscriber.onError(IOException())
                    }
                }
            })
        })
            .map { text -> text.toLowerCase().trim() }
            .debounce(250, TimeUnit.MILLISECONDS)
            .distinct()
            .filter { text -> text.isNotBlank() }
            .subscribe { text ->
                searchPresenter!!.searchEngine(text)
            }
    }

    private fun initViews() {
        searchbar = findViewById(R.id.searchToolbar)
        searchMoviesText = findViewById(R.id.searchMoviesText)
        searchShimmer = findViewById(R.id.searchListShimmerContainer)
        searchRecyclerView = findViewById(R.id.searchMovieList)
    }

    private fun initPresenter() {
        searchPresenter = SearchPresenter(this, this)
    }

    private fun isFirstTimeRunning(savedInstanceState: Bundle?): Boolean {
        return savedInstanceState == null
    }

    override fun onResume() {
        super.onResume()
        searchShimmer!!.startShimmer()
    }

    override fun onPause() {
        searchShimmer!!.stopShimmer()
        observable!!.dispose()
        super.onPause()
    }

    override fun finish() {
        // when the user tries to finish the activity we have to animate the exit
        // let's start by hiding the keyboard so that the exit seems smooth
        hideKeyboard()

        // at the same time, start the exit transition
        exitTransitionWithAction(Runnable {
            // which finishes the activity (for real) when done
            super@SearchActivity.finish()

            // override the system pending transition as we are handling ourselves
            overridePendingTransition(0, 0)
        })
    }

    private fun exitTransitionWithAction(endingAction: Runnable) {
        val transition = FadeOutTransition.withAction(object : SimpleTransitionListener() {
            override fun onTransitionEnd(transition: Transition) {
                endingAction.run()
            }
        })

        TransitionManager.beginDelayedTransition(searchbar, transition)
        searchbar!!.hideContent()
    }

    override fun setVisibilityProgressBar(visibility: Int) {
        runOnUiThread {
            searchShimmer!!.visibility = visibility
            when (visibility) {
                View.GONE -> {
                    searchRecyclerView!!.visibility = View.VISIBLE
                    Handler().postDelayed({ searchRecyclerView!!.scrollToPosition(0) }, 200)
                }
                View.VISIBLE -> {
                    searchShimmer!!.startShimmer()
                    searchRecyclerView!!.visibility = View.GONE
                }
            }
        }
    }

    override fun setRecyclerViewAdapter(adapter: RecyclerView.Adapter<*>) {
        val linearLayoutManager = GridLayoutManager(this, 2)
        searchRecyclerView!!.layoutManager = linearLayoutManager
        searchRecyclerView!!.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        } else if (item.itemId == R.id.action_clear) {
            searchbar!!.clearText()
            searchPresenter!!.clearAdapter()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}