package com.tjhello.page

import android.content.Intent
import android.view.View
import androidx.annotation.LayoutRes

interface IPageActivityMethod {

    fun setIntent(intent: Intent)

    fun getIntent():Intent

    fun startActivity(intent: Intent)

    fun finish()

    fun setContentView(@LayoutRes layoutId:Int)

    fun setContentView(view:View)

}