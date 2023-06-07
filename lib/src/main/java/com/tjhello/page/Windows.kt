package com.tjhello.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

open class Windows(private val layout: FrameLayout) {

    private val mDecorView: ViewGroup = layout

    open fun setContentView(view: View) {
        mDecorView.removeAllViews()
        mDecorView.addView(view)
    }

    open fun setContentView(layoutId: Int) {
        mDecorView.removeAllViews()
        val view = LayoutInflater.from(layout.context).inflate(layoutId, null)
        mDecorView.addView(view)
    }

    fun getDecorView() = mDecorView

}