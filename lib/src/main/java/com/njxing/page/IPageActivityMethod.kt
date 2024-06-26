package com.njxing.page

import android.app.Application
import android.app.LoaderManager
import android.content.Intent
import android.content.res.Resources
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.AnimRes
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity

interface IPageActivityMethod {

    fun setIntent(intent: Intent)

    fun getIntent():Intent

    fun startActivity(intent: Intent)

    fun startActivity(clazz: Class<out PageActivity>)

    fun finish()

    fun setContentView(@LayoutRes layoutId:Int)

    fun setContentView(view:View)

    fun startActivityForResult(intent: Intent,requestCode:Int)

    fun startActivityForResult(clazz: Class<out PageActivity>,requestCode: Int)

    fun setResult(resultCode:Int,intent: Intent?=null)

    fun setRequestCode(code:Int)

    fun getDocker():PageDocker

    fun getActivity():AppCompatActivity

    fun getString(@StringRes resId:Int):String

    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String

    fun getColor(@ColorRes resId:Int):Int

    fun getResources():Resources

    @Deprecated("see onPreEnterStartAnim、onPreEnterResumeAnim、onPreExitFinishAnim、onPreExitPauseAnim")
    fun overridePendingTransition(@AnimRes enterAnim: Int,@AnimRes exitAnim: Int)

    fun isFinishing():Boolean

    fun runOnUiThread(runnable: Runnable)

    fun getWindow(): Window

    fun getWindowManager():WindowManager

    fun getApplication():Application

    @Deprecated("Use androidx.fragment.app.FragmentActivity.getSupportLoaderManager()")
    fun getLoaderManager(): LoaderManager

    fun getCurrentFocus():View?

    fun getSystemService(name:String):Any?
}