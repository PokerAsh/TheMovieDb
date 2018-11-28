package com.yernarkt.themoviedb.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import java.util.ArrayList

class ViewPagerAdapter: FragmentStatePagerAdapter {
    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()

    constructor(fm: FragmentManager?) : super(fm)

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }

    override fun getItemPosition(any: Any): Int {
        return if (any.javaClass.isAssignableFrom(Fragment::class.java)) {
            val customFragment = any as Fragment
            mFragmentList.indexOf(customFragment)
        } else {
            PagerAdapter.POSITION_NONE
        }
    }
}