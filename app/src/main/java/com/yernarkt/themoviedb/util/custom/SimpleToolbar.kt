package com.yernarkt.themoviedb.util.custom

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import com.yernarkt.themoviedb.R

class SimpleToolbar(context: Context, attrs: AttributeSet) : TransformingToolbar(context, attrs) {
    init {
        setBackgroundColor(ContextCompat.getColor(context, R.color.white))
    }
}