package com.yernarkt.themoviedb.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.Toolbar
import android.transition.Transition
import android.transition.TransitionManager
import android.view.Menu
import android.view.MenuItem
import com.yernarkt.themoviedb.R
import com.yernarkt.themoviedb.ui.fragment.MovieDetailFragment
import com.yernarkt.themoviedb.ui.fragment.MoviesBaseFragment
import com.yernarkt.themoviedb.util.REQUEST_CODE_SEARCH_ACTIVITY
import com.yernarkt.themoviedb.util.transition.FadeInTransition
import com.yernarkt.themoviedb.util.transition.FadeOutTransition
import com.yernarkt.themoviedb.util.transition.SimpleTransitionListener

class MovieBaseActivity : SomeUtilityActivity() {
    private var mainToolbar: Toolbar? = null
    private var movieId: String? = null
    private var movieTitle: String? = null

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
                        movieTitle!!
                    )
                )
                .addToBackStack(null)
                .commit()
        }
    }

    private fun fadeToolbarIn() {
        TransitionManager.beginDelayedTransition(mainToolbar, FadeInTransition.createTransition())
        val layoutParams = mainToolbar!!.layoutParams as AppBarLayout.LayoutParams
        layoutParams.setMargins(8, 8, 8, 8)
        mainToolbar!!.layoutParams = layoutParams
    }

    private fun transitionToSearch() {
        val transition = FadeOutTransition.withAction(navigateToSearchWhenDone())

        TransitionManager.beginDelayedTransition(mainToolbar, transition)
        val frameLP = mainToolbar!!.layoutParams as AppBarLayout.LayoutParams
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
        }
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.main_action_search) {
            showKeyboard()
            transitionToSearch()
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
