package com.tjhello.page

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

abstract class PageDocker : AppCompatActivity() {

    /**
     * 一切之始
     */
    abstract fun onCreate()

    private lateinit var mDockerLayout : FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PageController.bindDocker(this)
        injectRootLayout()
        onCreate()

    }

    override fun onDestroy() {
        super.onDestroy()
        PageController.unBindDocker(this)
    }

    override fun onBackPressed() {
        if(mDockerLayout.childCount>0){
            val pageActivity = mDockerLayout.getChildAt(mDockerLayout.childCount-1) as PageActivity
            pageActivity.onBackPressed()
        }else{
            super.onBackPressed()
        }
    }

    private fun injectRootLayout(){
        mDockerLayout = FrameLayout(this)
        this.window.addContentView(mDockerLayout,
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT))
    }


    fun startPageActivity(pageActivity: PageActivity,intent: Intent){
        pageActivity.setIntent(intent)
        pageActivity.onCreate(null)
        pageActivity.onPostCreate()
        pageActivity.onStart()
        pageActivity.onResume()
        pageActivity.onPostResume()
        mDockerLayout.addView(pageActivity)
    }

    fun finishPageActivity(pageActivity: PageActivity){
        pageActivity.onPause()
        mDockerLayout.removeView(pageActivity)
        pageActivity.onStop()
        pageActivity.onDestroy()
    }



}