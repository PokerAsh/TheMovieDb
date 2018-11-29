package com.yernarkt.themoviedb.ui.activities

import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionManager
import android.view.Menu
import android.view.MenuItem
import android.view.ViewTreeObserver
import android.widget.EditText
import com.yernarkt.themoviedb.R
import com.yernarkt.themoviedb.util.custom.CustomSearchBar
import com.yernarkt.themoviedb.util.transition.SimpleTransitionListener
import com.yernarkt.themoviedb.util.transition.FadeInTransition
import com.yernarkt.themoviedb.util.transition.FadeOutTransition

class SearchActivity : SomeUtilityActivity() {
    private var searchbar: CustomSearchBar? = null
    private var searchMoviesText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchbar = findViewById(R.id.searchToolbar)
        searchMoviesText = findViewById(R.id.searchMoviesText)
        setSupportActionBar(searchbar)

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

    private fun isFirstTimeRunning(savedInstanceState: Bundle?): Boolean {
        return savedInstanceState == null
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
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}