package com.tjhello.page

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View

import android.widget.FrameLayout

open class PageActivity(context: Context) : FrameLayout(context),IPageActivityLifecycle,IPageActivityMethod,IPageActivityFunction {

    companion object{
        const val RESULT_OK = -1
        const val RESULT_FIRST_USER = 1
        const val RESULT_CANCELED = 0
    }

    private val mPageDocker = context as PageDocker
    private var mIntent = Intent()
    private var mWhereFromKey : Int = 0
    private var mRequestCode : Int = 0
    private var mResultCode = RESULT_CANCELED
    private var mResultIntent : Intent ?= null

    init {
        this.setBackgroundColor(Color.WHITE)
    }

    override fun onUserLeaveHint() {

    }

    override fun onTrimMemory(level: Int) {
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

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
        mPageDocker.startPageActivity(this,intent)
    }

    override fun finish() {
        mPageDocker.finishPageActivity(this)
    }

    override fun setContentView(layoutId: Int) {
        setContentView(LayoutInflater.from(context).inflate(layoutId,null))
    }

    override fun setContentView(view: View) {
        this.removeAllViews()
        this.addView(view)
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        mPageDocker.startPageActivity(this,intent,requestCode)
    }

    override fun setResult(resultCode: Int, intent: Intent?) {
        mResultCode = resultCode
        mResultIntent = intent
    }

    override fun setRequestCode(code: Int) {
        mRequestCode = code
    }

    override fun getDocker(): PageDocker {
        return mPageDocker
    }

    override fun getString(id: Int): String {
        return context.getString(id)
    }

    override fun getColor(id: Int): Int {
        return context.resources.getColor(id)
    }

    override fun onFocusChanged(gainFocus: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
        if(gainFocus){
            onResume()
            onPostResume()
        }else{
            onPause()
        }
        onWindowFocusChanged(gainFocus)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onDestroy()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }

    internal fun setWhereFromKey(key:Int){
        mWhereFromKey = key
    }

    internal fun getWhereFromKey():Int{
        return mWhereFromKey
    }

    internal fun getRequestCode():Int{
        return mRequestCode
    }

    fun getResultCode():Int{
        return mResultCode
    }

    fun getResultIntent():Intent?{
        return mResultIntent
    }


}