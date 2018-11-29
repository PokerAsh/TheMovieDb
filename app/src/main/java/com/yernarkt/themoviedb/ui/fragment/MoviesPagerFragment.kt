package com.yernarkt.themoviedb.ui.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yernarkt.themoviedb.R
import com.yernarkt.themoviedb.adapter.ViewPagerAdapter
import com.yernarkt.themoviedb.ui.activities.MovieBaseActivity

class MoviesPagerFragment : Fragment() {
    private lateinit var baseActivity: MovieBaseActivity
    private lateinit var mView: View
    private lateinit var mAdapter: ViewPagerAdapter

    private var pagerTabLayout: TabLayout? = null
    private var pagerViewPager: ViewPager? = null

    private var viewPagerPosition = 0

    private fun onTabSelectedListener(viewPager: ViewPager): TabLayout.OnTabSelectedListener {
        return object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        }
    }

    companion object {
        fun newInstance(): MoviesPagerFragment {
            return MoviesPagerFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity = context as MovieBaseActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_movies_pager, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupViewPager(pagerViewPager)

        pagerTabLayout!!.setupWithViewPager(pagerViewPager)
        pagerTabLayout!!.addOnTabSelectedListener(onTabSelectedListener(pagerViewPager!!))
        pagerTabLayout!!.tabMode = TabLayout.MODE_FIXED
    }

    private fun initViews(view: View) {
        pagerTabLayout = view.findViewById(R.id.pagerTabLayout)
        pagerViewPager = view.findViewById(R.id.pagerViewPager)
    }

    private fun setupViewPager(containerViewPager: ViewPager?) {
        mAdapter = ViewPagerAdapter(childFragmentManager)

        mAdapter.addFragment(MoviesListFragment.newInstance("Popular"), getString(R.string.s_popular))
        mAdapter.addFragment(MoviesListFragment.newInstance("Upcoming"), getString(R.string.s_soon))

        containerViewPager!!.adapter = mAdapter
        containerViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(pagerTabLayout))
        containerViewPager.currentItem = 0
    }

    override fun onResume() {
        super.onResume()
        pagerViewPager!!.currentItem = viewPagerPosition


    }

    override fun onPause() {
        super.onPause()
        viewPagerPosition = pagerViewPager!!.currentItem
    }
}