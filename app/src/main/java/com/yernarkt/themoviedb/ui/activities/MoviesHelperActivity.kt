package com.yernarkt.themoviedb.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.yernarkt.themoviedb.R
import com.yernarkt.themoviedb.ui.fragment.MoviesFragment
import com.yernarkt.themoviedb.util.EXTRA_TAG
import com.yernarkt.themoviedb.util.GENRE_ID

class MoviesHelperActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private var tag: String? = null
    private var genreId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies_helper)

        initToolbar()
        switchFragment()
    }

    private fun initToolbar() {
        toolbar = findViewById(R.id.helperToolbar)
        setSupportActionBar(toolbar)
    }

    private fun switchFragment() {
        tag = intent.getStringExtra(EXTRA_TAG)
        genreId = intent.getStringExtra(GENRE_ID)
        when (tag) {
            "MovieList" -> showFragment(MoviesFragment.newInstance("Genre", genreId))
        }
    }

    private fun showFragment(fragment: Fragment? = null) {
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.helper_container, fragment)
                .commit()
        }
    }

    companion object {
        fun startActivity(appCompatActivity: AppCompatActivity, tag: String) {
            val intent = Intent(appCompatActivity, MoviesHelperActivity::class.java)
            intent.putExtra(EXTRA_TAG, tag)
            appCompatActivity.startActivity(intent)
        }

        fun startActivity(appCompatActivity: AppCompatActivity, tag: String, genreId: String) {
            val intent = Intent(appCompatActivity, MoviesHelperActivity::class.java)
            intent.putExtra(EXTRA_TAG, tag)
            intent.putExtra(GENRE_ID, genreId)
            appCompatActivity.startActivity(intent)
        }
    }
}