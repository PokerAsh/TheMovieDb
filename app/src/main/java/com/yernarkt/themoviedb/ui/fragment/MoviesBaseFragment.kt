package com.yernarkt.themoviedb.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.yernarkt.themoviedb.R
import com.yernarkt.themoviedb.ui.activities.MovieBaseActivity

class MoviesBaseFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var activity: MovieBaseActivity
    private var mView: View? = null
    private lateinit var bottomNavBar: BottomNavigationView
    private var position: Int = 0

    companion object {
        fun newInstance(): MoviesBaseFragment {
            return MoviesBaseFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        switchFragment(MoviesPagerFragment.newInstance())
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity = context as MovieBaseActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_movies_base, container, false)
        return mView
    }

    private fun initViews() {
        bottomNavBar = mView!!.findViewById(R.id.bottomNavigationView)
        bottomNavBar.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        var fragment: Fragment? = null

        when (menuItem.itemId) {
            R.id.navigation_movies -> {
                fragment = MoviesPagerFragment.newInstance()
            }
            R.id.navigation_genres -> fragment = GenresListFragment.newInstance()
        }

        return switchFragment(fragment)
    }

    override fun onPause() {
        super.onPause()
        position = bottomNavBar.selectedItemId
    }

    override fun onResume() {
        super.onResume()
        bottomNavBar.selectedItemId = position
    }

    private fun switchFragment(fragment: Fragment? = null): Boolean {
        if (fragment != null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.mainFrameLayout, fragment)
                .commit()
            return true
        }

        return false
    }
}