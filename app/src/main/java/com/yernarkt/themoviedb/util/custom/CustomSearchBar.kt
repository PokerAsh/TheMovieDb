package com.yernarkt.themoviedb.util.custom

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.widget.EditText
import com.yernarkt.themoviedb.R

class CustomSearchBar(context: Context, attrs: AttributeSet) : TransformingToolbar(context, attrs) {
    private var editText: AppCompatEditText? = null

    init {
        setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        setNavigationIcon(R.drawable.ic_arrow_back)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        inflate(context, R.layout.merge_search, this)
        editText = findViewById(R.id.searchMoviesText)
    }

    override fun showContent() {
        super.showContent()
        editText!!.requestFocus()
    }

    fun clearText() {
        editText!!.text = null
    }
}