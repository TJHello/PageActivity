package com.tjhello.page

import android.app.Application
import android.app.LoaderManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Parcelable
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.eyewind.lib.log.EyewindLog

open class BasePageActivity(private val context:Context) : FrameLayout(context),IPageActivityLifecycle,IPageActivityMethod {

    companion object{
        const val RESULT_OK = -1
        const val RESULT_FIRST_USER = 1
        const val RESULT_CANCELED = 0
        const val SAVED_KEY_SUPPER_DATA = "__super_data"
        private const val TAG = "BasePageActivity"
    }

    private val mPageDocker = context as PageDocker
    private var mIntent = Intent()
    private var mWhereFromKey : Int = 0
    private var mRequestCode : Int = 0
    private var mResultCode = RESULT_CANCELED
    private var mResultIntent : Intent ?= null
    private var mIsFinish = false
    val windows = PageActivityWindows(this)
    private var mEnableAnim = true//是否启动动画
    private var mEnterAnim = 0
    private var mExitAnim = 0


    @CallSuper
    protected open fun onCreate(savedInstanceState:Bundle?){
        log("[onCreate]")
    }

    @CallSuper
    protected open fun onResume(){
        log("[onResume]")
    }

    @CallSuper
    protected open fun onPause(){
        log("[onPause]")
    }

    @CallSuper
    protected open fun onDestroy(){
        log("[onDestroy]")
    }

    @CallSuper
    protected open fun onStart(){
        log("[onStart]")
    }

    @CallSuper
    protected open fun onStop(){
        log("[onStop]")
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        log("[onWindowFocusChanged]$hasWindowFocus")
    }

    @CallSuper
    open fun onPostCreate(savedInstanceState:Bundle?){
//        log("[onPostCreate]")
    }

    @CallSuper
    open fun onPostResume(){
//        log("[onPostResume]")
    }

    //长按Home键或者点击Home键的时候触发
    @CallSuper
    protected open fun onUserLeaveHint(){
        log("[onUserLeaveHint]")
    }

    //内存状态回调
    @CallSuper
    open fun onTrimMemory(level:Int){
        log("[onTrimMemory]level:$level")
    }

    //二次启动Activity的时候
    @CallSuper
    protected open fun onNewIntent(intent: Intent){
        log("[onNewIntent]")
    }

    @CallSuper
    protected open fun onRequestPermissionsResult(
        requestCode:Int,
        permissions:Array<out String>,
        grantResults:IntArray
    ){
        log("[onRequestPermissionsResult]")
    }

    @CallSuper
    protected open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        log("[onActivityResult]requestCode=$requestCode,resultCode=$resultCode,data=$data")
    }

    protected open fun onSaveInstanceState(outState:Bundle){
        log("[onSaveInstanceState]")
    }

    protected open fun onRestoreInstanceState(savedInstanceState:Bundle){
        log("[onRestoreInstanceState]")
    }

    final override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        val parcelable = super.onSaveInstanceState()
        onSaveInstanceState(bundle)
        if(parcelable!=null){
            bundle.putParcelable(SAVED_KEY_SUPPER_DATA,parcelable)
        }
        getDocker().savePageActivity(this,bundle)
        return bundle
    }

    final override fun onRestoreInstanceState(state: Parcelable?) {
        log("[onRestoreInstanceState]")
        if(state==null) return
        val bundle = state as Bundle
        val superData = bundle.getParcelable<Parcelable>(SAVED_KEY_SUPPER_DATA)
        super.onRestoreInstanceState(superData)
        onRestoreInstanceState(bundle)
    }

    fun onPreEnterStartAnim(function:()->Unit){
        if(mEnableAnim){
            translationX = getScreenWidth().toFloat()
            animate().translationX(0f).onEnd {
                function()
            }
        }else{
            function()
        }
    }

    fun onPreEnterResumeAnim(function:()->Unit){
        if(mEnableAnim){
            animate().translationX(0f).onEnd {
                function()
            }
        }else{
            function()
        }
    }

    fun onPreExitFinishAnim(function: () -> Unit){
        if(mEnableAnim){
            animate().translationX(getScreenWidth().toFloat()).onEnd {
                function()
            }
        }else{
            function()
        }
    }

    fun onPreExitPauseAnim(function: () -> Unit){
        if(mEnableAnim){
            animate().translationX(-getScreenWidth().toFloat()).onEnd {
                function()
            }
        }else{
            function()
        }
    }

    open fun onBackPressed(){
        log("[onBackPressed]")
        finish()
    }

    override fun performBackPressed() {
        val result = windows.foreachDialog {
            return@foreachDialog it.onBackPressed()
        }
        if(!result){
            onBackPressed()
        }
    }

    override fun performCreate(savedInstanceState:Parcelable?) {
//        log("[performCreate]")
        if(savedInstanceState!=null&&savedInstanceState is Bundle){
            onCreate(savedInstanceState)
            onPostCreate(savedInstanceState)
        }else {
            onCreate(null)
            onPostCreate(null)
        }
    }

    override fun performNewIntent(intent: Intent) {
//        log("[performNewIntent]")
        onNewIntent(intent)
    }

    override fun performStart() {
//        log("[performStart]")
        onStart()
    }

    override fun performRestart() {
//        log("[performRestart]")
        performStart()
    }

    override fun performResume() {
//        log("[performResume]")
        onResume()
        onPostResume()
    }

    override fun performPause() {
//        log("[performPause]")
        onPause()
    }

    override fun performUserLeaving() {
//        log("[performUserLeaving]")
        onUserLeaveHint()
    }

    override fun performStop() {
//        log("[performStop]")
        onStop()
    }

    override fun performDestroy() {
        log("[performDestroy]")
        onDestroy()
    }

    override fun dispatchActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        log("[dispatchActivityResult]")
        onActivityResult(requestCode, resultCode, data)
    }

    override fun dispatchRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        log("[dispatchRequestPermissionsResult]")
        onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun dispatchSaveInstanceState(): Parcelable? {
        return onSaveInstanceState()
    }

    override fun dispatchRestoreInstanceState(state: Parcelable?) {
        return onRestoreInstanceState(state)
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
        windows.setContentView(layoutId)
    }

    override fun setContentView(view: View) {
        windows.setContentView(view)
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


    @Deprecated("please refer to onPreEnterStartAnim、onPreEnterResumeAnim、onPreExitFinishAnim、onPreExitPauseAnim")
    override fun overridePendingTransition(enterAnim: Int, exitAnim: Int) {
        mEnterAnim = enterAnim
        mEnterAnim = exitAnim
    }

    override fun isFinishing(): Boolean {
        return mIsFinish
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

    override fun getWindow(): Window {
        return mPageDocker.window
    }

    override fun getWindowManager(): WindowManager {
        return mPageDocker.windowManager
    }

    override fun getApplication(): Application {
        return mPageDocker.application
    }

    override fun getLoaderManager(): LoaderManager {
        return mPageDocker.loaderManager
    }

    override fun getCurrentFocus(): View? {
        return mPageDocker.currentFocus
    }

    override fun getSystemService(name: String): Any? {
        return mPageDocker.getSystemService(name)
    }

    private fun log(msg:String){
        EyewindLog.logLibInfo(this::class.java.simpleName,msg)
    }

    fun getScreenWidth(): Int {
        return context.resources.displayMetrics.widthPixels
    }

    fun getScreenHeight(): Int {
        return context.resources.displayMetrics.heightPixels
    }

    fun enableAnim(bool:Boolean){
        mEnableAnim = bool
    }
}