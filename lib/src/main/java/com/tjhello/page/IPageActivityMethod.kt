package com.tjhello.page

import android.content.Intent
import android.content.res.Resources
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes

interface IPageActivityMethod {

    fun setIntent(intent: Intent)

    fun getIntent():Intent

    fun startActivity(intent: Intent)

    fun finish()

    fun setContentView(@LayoutRes layoutId:Int)

    fun setContentView(view:View)

    fun startActivityForResult(intent: Intent,requestCode:Int)

    fun setResult(resultCode:Int,intent: Intent?=null)

    fun setRequestCode(code:Int)

    fun getDocker():PageDocker

    fun getString(@StringRes id:Int):String

    fun getColor(@ColorRes id:Int):Int

}