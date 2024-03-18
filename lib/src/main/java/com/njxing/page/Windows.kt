package com.njxing.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

open class Windows(private val layout: ViewGroup):FrameLayout(layout.context) {

    private var mDecorView: ViewGroup = layout

    init {
        this.addView(layout, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    open fun setContentView(view: View) {
        mDecorView.removeAllViews()
        mDecorView.addView(view)
    }

    open fun setContentView(layoutId: Int) {
        mDecorView.removeAllViews()
        val view = LayoutInflater.from(layout.context).inflate(layoutId, null,false)
        mDecorView.addView(view)
    }

    fun getDecorView() = mDecorView

}