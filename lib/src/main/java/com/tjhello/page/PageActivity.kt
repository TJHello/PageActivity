package com.tjhello.page

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View

import android.widget.FrameLayout

open class PageActivity(context: Context) : FrameLayout(context),IPageActivityLifecycle,IPageActivityMethod,IPageActivityFunction {

    private val mPageDocker = context as PageDocker
    private var mIntent = Intent()

    init {
        this.setBackgroundColor(Color.WHITE)
    }

    override fun onUserLeaveHint() {

    }

    override fun onTrimMemory(level: Int) {
    }

    override fun onNewIntent(intent: Intent) {
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    override fun onDestroy() {
    }

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun onPostCreate() {
    }

    override fun onPostResume() {
    }

    override fun setIntent(intent: Intent) {
        mIntent = intent
    }

    override fun getIntent(): Intent {
        return mIntent
    }

    override fun startActivity(intent: Intent) {
        PageController.startPageActivity(mPageDocker,intent)
    }

    override fun finish() {
        PageController.finishPageActivity(mPageDocker,this)
    }

    override fun setContentView(layoutId: Int) {
        setContentView(LayoutInflater.from(context).inflate(layoutId,null))
    }

    override fun setContentView(view: View) {
        this.removeAllViews()
        this.addView(view)
    }

    override fun onFocusChanged(gainFocus: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
        onWindowFocusChanged(gainFocus)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }

}