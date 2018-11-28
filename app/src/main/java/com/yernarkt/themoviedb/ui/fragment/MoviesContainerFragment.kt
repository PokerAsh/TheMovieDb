package com.yernarkt.themoviedb.ui.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yernarkt.themoviedb.R
import com.yernarkt.themoviedb.adapter.ViewPagerAdapter
import com.yernarkt.themoviedb.ui.activities.MainActivity

class MoviesContainerFragment : Fragment() {
    private lateinit var activity: MainActivity
    private lateinit var mView: View
    private lateinit var mAdapter: ViewPagerAdapter

    private var containerToolbar: Toolbar? = null
    private var containerTabLayout: TabLayout? = null
    private var containerViewPager: ViewPager? = null

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
        fun newInstance(): MoviesContainerFragment {
            return MoviesContainerFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = context as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_movies_container, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupToolbar()

        setupViewPager(containerViewPager)

        containerTabLayout!!.setupWithViewPager(containerViewPager)
        containerTabLayout!!.addOnTabSelectedListener(onTabSelectedListener(containerViewPager!!))
        containerTabLayout!!.tabMode = TabLayout.MODE_FIXED
    }

    private fun initViews(view: View) {
        containerToolbar = view.findViewById(R.id.containerToolbar)
        containerTabLayout = view.findViewById(R.id.containerTabLayout)
        containerViewPager = view.findViewById(R.id.containerViewPager)
    }

    private fun setupToolbar() {
        activity.setSupportActionBar(containerToolbar)
        activity.supportActionBar!!.title = activity.resources.getString(R.string.s_movies)
    }

    private fun setupViewPager(containerViewPager: ViewPager?) {
        mAdapter = ViewPagerAdapter(childFragmentManager)

        mAdapter.addFragment(MoviesPopularFragment.newInstance(), getString(R.string.s_popular))
        mAdapter.addFragment(MoviesSoonFragment.newInstance(), getString(R.string.s_soon))

        containerViewPager!!.adapter = mAdapter
        containerViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(containerTabLayout))
        containerViewPager.currentItem = 0
    }

    override fun onResume() {
        super.onResume()
        if (viewPagerPosition != 0)
            containerViewPager!!.currentItem = viewPagerPosition


    }

    override fun onPause() {
        super.onPause()
        viewPagerPosition = containerViewPager!!.currentItem
    }
}