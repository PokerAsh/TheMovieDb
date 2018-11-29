package com.yernarkt.themoviedb.ui.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.yernarkt.themoviedb.R
import com.yernarkt.themoviedb.ui.fragment.GenresFragment
import com.yernarkt.themoviedb.ui.fragment.MoviesContainerFragment

class MovieBaseActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var bottomNavBar: BottomNavigationView
    private var mainToolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        switchFragment(MoviesContainerFragment.newInstance())
        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(mainToolbar)

    }

    private fun initViews() {
        mainToolbar = findViewById(R.id.mainToolbar)
        bottomNavBar = findViewById(R.id.bottomNavigationView)
        setSupportActionBar(mainToolbar)
        bottomNavBar.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        var fragment: Fragment? = null

        when (menuItem.itemId) {
            R.id.navigation_movies -> fragment = MoviesContainerFragment.newInstance()
            R.id.navigation_genres -> fragment = GenresFragment.newInstance()
        }

        return switchFragment(fragment)
    }

    private fun switchFragment(fragment: Fragment? = null): Boolean {
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainFrameLayout, fragment)
                .commit()
            return true
        }

        return false
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount

        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}
