package com.tjhello.page

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity

open class BasePageActivity(private val context:Context) : FrameLayout(context),IPageActivityLifecycle,IPageActivityMethod {

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
    private var mIsFinish = false

    @CallSuper
    protected open fun onCreate(savedInstanceState:Bundle?){}

    @CallSuper
    protected open fun onResume(){}

    @CallSuper
    protected open fun onPause(){}

    @CallSuper
    protected open fun onDestroy(){}

    @CallSuper
    protected open fun onStart(){}

    @CallSuper
    protected open fun onStop(){}

    @CallSuper
    open fun onPostCreate(savedInstanceState:Bundle?){}

    @CallSuper
    open fun onPostResume(){}

    //长按Home键或者点击Home键的时候触发
    @CallSuper
    protected open fun onUserLeaveHint(){}

    //内存状态回调
    @CallSuper
    open fun onTrimMemory(level:Int){

    }

    //二次启动Activity的时候
    @CallSuper
    protected open fun onNewIntent(intent: Intent){}

    @CallSuper
    protected open fun onRequestPermissionsResult(
        requestCode:Int,
        permissions:Array<out String>,
        grantResults:IntArray
    ){}

    @CallSuper
    protected open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){}

    protected open fun onSaveInstanceState(outState:Bundle){

    }

    protected open fun onRestoreInstanceState(savedInstanceState:Bundle){

    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        val parcelable = super.onSaveInstanceState()
        onSaveInstanceState(bundle)
        bundle.putParcelable("__super_data",parcelable)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if(state==null) return
        val bundle = state as Bundle
        val superData = bundle.getParcelable<Parcelable>("__super_data")
        super.onRestoreInstanceState(superData)
        onRestoreInstanceState(bundle)
    }

    open fun onBackPressed(){
        finish()
    }


    override fun performCreate(savedInstanceState:Bundle?) {
        onCreate(savedInstanceState)
        onPostCreate(savedInstanceState)
    }

    override fun performNewIntent(intent: Intent) {
        onNewIntent(intent)
    }

    override fun performStart() {
        onStart()
    }

    override fun performRestart() {
        performStart()
    }

    override fun performResume() {
        onResume()
        onPostResume()
    }

    override fun performPause() {
        onPause()
    }

    override fun performUserLeaving() {
        onUserLeaveHint()
    }

    override fun performStop() {
        onStop()
    }

    override fun performDestroy() {
        onDestroy()
    }

    override fun dispatchActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        onActivityResult(requestCode, resultCode, data)
    }

    override fun dispatchRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        onRequestPermissionsResult(requestCode, permissions, grantResults)
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

    override fun startActivity(clazz: Class<out PageActivity>) {
        val intent = Intent(context,clazz)
        startActivity(intent)
    }

    override fun finish() {
        mPageDocker.finishPageActivity(this)
        mIsFinish = true
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

    override fun startActivityForResult(clazz: Class<out PageActivity>, requestCode: Int) {
        val intent = Intent(context,clazz)
        startActivityForResult(intent,requestCode)
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

    override fun getActivity(): AppCompatActivity {
        return mPageDocker
    }

    override fun getString(resId: Int): String {
        return context.getString(resId)
    }

    override fun getString(resId: Int, vararg formatArgs: Any): String {
        return context.getString(resId, formatArgs)
    }

    override fun getColor(resId: Int): Int {
        return context.resources.getColor(resId)
    }

    override fun getResources(): Resources {
        return context.resources
    }

    override fun overridePendingTransition(enterAnim: Int, exitAnim: Int) {

    }

    override fun isFinishing(): Boolean {
        return mIsFinish
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


    override fun runOnUiThread(runnable: Runnable) {
        mPageDocker.runOnUiThread(runnable)
    }

}