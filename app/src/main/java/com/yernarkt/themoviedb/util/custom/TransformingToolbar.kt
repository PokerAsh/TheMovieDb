package com.yernarkt.themoviedb.util.custom

import android.content.Context
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.view.View

open class TransformingToolbar(context: Context, attrs: AttributeSet) : Toolbar(context, attrs) {

    /**
     * Sets the Visibility of all children to GONE
     */
    fun hideContent() {
        for (i in 0 until childCount) {
            getChildAt(i).visibility = View.GONE
        }
    }

    /**
     * Sets the Visibility of all children to VISIBLE
     */
    open fun showContent() {
        for (i in 0 until childCount) {
            getChildAt(i).visibility = View.VISIBLE
        }
    }

}