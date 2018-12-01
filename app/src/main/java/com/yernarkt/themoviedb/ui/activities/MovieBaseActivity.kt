package com.yernarkt.themoviedb.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.Toolbar
import android.transition.Transition
import android.transition.TransitionManager
import android.view.Menu
import android.view.MenuItem
import com.yernarkt.themoviedb.R
import com.yernarkt.themoviedb.ui.fragment.MovieDetailFragment
import com.yernarkt.themoviedb.ui.fragment.MoviesBaseFragment
import com.yernarkt.themoviedb.ui.fragment.MoviesSortDialogFragment
import com.yernarkt.themoviedb.util.REQUEST_CODE_SEARCH_ACTIVITY
import com.yernarkt.themoviedb.util.transition.FadeInTransition
import com.yernarkt.themoviedb.util.transition.FadeOutTransition
import com.yernarkt.themoviedb.util.transition.SimpleTransitionListener

class MovieBaseActivity : SomeUtilityActivity() {
    private var mainToolbar: Toolbar? = null
    private var movieId: String? = null
    private var movieTitle: String? = null
    private var moviePoster: String? = null
    private var movieOverview: String? = null
    private var toolbarMargin: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_base)

        initViews()
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_container, MoviesBaseFragment.newInstance())
            .commit()
    }

    private fun initViews() {
        mainToolbar = findViewById(R.id.mainToolbar)
        setSupportActionBar(mainToolbar)
        supportActionBar!!.title = "Поиск"
        supportActionBar!!.setLogo(R.drawable.ic_action_search)

        toolbarMargin = resources.getDimensionPixelSize(R.dimen.toolbarMargin)

        mainToolbar!!.setOnClickListener {
            showKeyboard()
            transitionToSearch()
        }
    }

    override fun onResume() {
        super.onResume()
        fadeToolbarIn()

        if (movieId != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.activity_container,
                    MovieDetailFragment.newInstance(
                        movieId!!,
                        movieTitle!!,
                        moviePoster!!,
                        movieOverview!!
                    )
                )
                .addToBackStack(null)
                .commit()
        }
    }

    private fun fadeToolbarIn() {
        TransitionManager.beginDelayedTransition(mainToolbar, FadeInTransition.createTransition())
        val layoutParams = mainToolbar!!.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.setMargins(48, toolbarMargin, toolbarMargin, toolbarMargin)
        mainToolbar!!.layoutParams = layoutParams
    }

    private fun transitionToSearch() {
        val transition = FadeOutTransition.withAction(navigateToSearchWhenDone())

        TransitionManager.beginDelayedTransition(mainToolbar, transition)
        val frameLP = mainToolbar!!.layoutParams as ConstraintLayout.LayoutParams
        frameLP.setMargins(0, 0, 0, 0)
        mainToolbar!!.layoutParams = frameLP
    }

    private fun navigateToSearchWhenDone(): Transition.TransitionListener {
        return object : SimpleTransitionListener() {
            override fun onTransitionEnd(transition: Transition) {
                val intent = Intent(this@MovieBaseActivity, SearchActivity::class.java)
                startActivityForResult(intent, REQUEST_CODE_SEARCH_ACTIVITY)
                overridePendingTransition(0, 0)
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_SEARCH_ACTIVITY && data != null) {
            movieId = data.getStringExtra("MovieId")
            movieTitle = data.getStringExtra("MovieTitle")
            moviePoster = data.getStringExtra("MoviePoster")
            movieOverview = data.getStringExtra("MovieOverview")
        }
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.main_action_filter) {
            MoviesSortDialogFragment.newInstance().show(supportFragmentManager, "filter")
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount

        if (count == 0) {
            super.onBackPressed()
        } else {
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            supportFragmentManager.popBackStack()
        }
    }
}
