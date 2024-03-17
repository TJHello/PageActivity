package com.njxing.page

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.contains
import com.njxing.page.info.PageHead
import java.util.Stack

abstract class PageDocker : AppCompatActivity() {

    companion object {
        private const val TAG = "PageDocker"
    }

    private var isStartIng = false//Docker启动中

    abstract fun onGetHomePage():Class<out BasePageActivity>

    abstract fun onPreInjectRootLayout()

    protected open fun onCustomDockerLayout():FrameLayout?{
        return null
    }

    private lateinit var mDockerLayout : FrameLayout
    private val pageHeadStack = Stack<PageHead>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isStartIng = true
        PageController.bindDocker(this)
        onPreInjectRootLayout()
        injectRootLayout()
        startPageActivity(null,Intent(this,onGetHomePage()))
    }

    override fun onPause() {
        super.onPause()
        if(!isStartIng){
            getTopPageActivity()?.activity?.performPause()
        }
    }

    override fun onResume() {
        super.onResume()
        if(!isStartIng){
            getTopPageActivity()?.activity?.performResume()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PageController.unBindDocker(this)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if(hasFocus){
            isStartIng = false
        }
        super.onWindowFocusChanged(hasFocus)
//        getTopPageActivity()?.activity?.onWindowFocusChanged(hasFocus)
    }

    override fun onBackPressed() {
        val pageActivity = getTopPageActivity()
        if(pageActivity==null){
            super.onBackPressed()
        }else{
            pageActivity.activity?.performBackPressed()
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        val pageActivity = getTopPageActivity()
        pageActivity?.activity?.performUserLeaving()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        foreachPageActivity{
            it.onTrimMemory(level)
        }
        releaseSomePages()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        foreachPageActivity{
            it.dispatchActivityResult(resultCode,resultCode,data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        foreachPageActivity{
            it.dispatchRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun injectRootLayout(){
        val customLayout = onCustomDockerLayout()
        if(customLayout==null){
            mDockerLayout = FrameLayout(this)
            this.window.addContentView(mDockerLayout,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT))
        }else{
            this.mDockerLayout = customLayout
        }
    }

    private fun getTopPageActivity():PageHead?{
        return if(pageHeadStack.isNotEmpty()){
            pageHeadStack.peek()
        }else{
            null
        }
    }

    private fun getPageActivity(clazz: Class<*>):BasePageActivity?{
        val index = findPageActivity{
            it::class.java.name==clazz.name
        } ?: return null
        val count = mDockerLayout.childCount
        for(i in count-1 downTo 0){
            val view = mDockerLayout.getChildAt(i)
            if(view is PageActivity){
                if(view.id == index.id){
                    return view
                }
            }
        }
        return newPageActivity(index)
    }

    private fun findPageActivity(function:(PageHead)->Boolean):PageHead?{
        for (pageIndex in pageHeadStack) {
            if(function(pageIndex)){
                return pageIndex
            }
        }
        return null
    }

    private fun foreachPageActivity(function:(BasePageActivity)->Unit){
        for (i in  mDockerLayout.childCount-1 downTo 0){
            val pageActivity = mDockerLayout.getChildAt(i) as BasePageActivity
            function(pageActivity)
        }
    }

    internal fun startPageActivity(pageFrom:BasePageActivity?,intent: Intent,requestCode: Int=0){
        val component = intent.component
        if(component!=null){
            val className = component.className
            val clazz = Class.forName(className)
            if(BasePageActivity::class.java.isAssignableFrom(clazz)){
                when(intent.flags){
                    //单例模式
                    Intent.FLAG_ACTIVITY_NEW_TASK->{
                        val pageActivityIndex = this.getTopPageActivity()
                        if(pageActivityIndex!=null&&pageActivityIndex.clazz.name==className){
                            //重启动，这个activity本来就在最前面了，不用做任何操作
                            onStartNewIntent(pageFrom,pageActivityIndex,intent,requestCode)
                        }else{
                            //新启动
                            val constructor = clazz.getConstructor(Context::class.java)
                            val page = constructor.newInstance(this) as BasePageActivity
                            page.id = View.generateViewId()
                            this.onStartPageActivity(pageFrom,PageHead(page),intent,requestCode)
                        }
                    }
                    //断背模式
                    Intent.FLAG_ACTIVITY_CLEAR_TOP->{
                        val pageActivity = this.getPageActivity(clazz)
                        if(pageActivity!=null&&pageActivity::class.java.name==className){
                            for (i in  mDockerLayout.childCount-1 downTo 0){
                                val page = mDockerLayout.getChildAt(i) as BasePageActivity
                                if(page.id==pageActivity.id){
                                    finishPageActivity(page)//结束掉自己,此处如果是单例模式可以不移除自己
                                    break
                                }
                                finishPageActivity(page)
                            }
                        }
                        val constructor = clazz.getConstructor(Context::class.java)
                        val page = constructor.newInstance(this) as BasePageActivity
                        page.id = View.generateViewId()
                        onStartPageActivity(pageFrom,PageHead(page),intent,requestCode)
                    }
                    //断背+复用模式
                    Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_SINGLE_TOP->{
                        val pageActivityIndex = this.findPageActivity {
                            it.clazz.name == clazz.name
                        }
                        if(pageActivityIndex!=null&&pageActivityIndex::class.java.name==className){
                            for (i in  mDockerLayout.childCount-1 downTo 0){
                                val page = mDockerLayout.getChildAt(i) as BasePageActivity
                                if(page.id==pageActivityIndex.id){
                                    break
                                }
                                finishPageActivity(page)
                            }
                            onStartNewIntent(pageFrom,pageActivityIndex, intent,requestCode)
                        }else{
                            val constructor = clazz.getConstructor(Context::class.java)
                            val page = constructor.newInstance(this) as BasePageActivity
                            page.id = View.generateViewId()
                            onStartPageActivity(pageFrom,PageHead(page),intent,requestCode)
                        }
                    }
                    //栈顶模式
                    0 or Intent.FLAG_ACTIVITY_SINGLE_TOP->{
                        val pageActivityIndex = getTopPageActivity()
                        if(pageActivityIndex!=null&&pageActivityIndex.clazz.name==className){
                            onStartNewIntent(pageFrom,pageActivityIndex,intent,requestCode)
                        }else{
                            val constructor = clazz.getConstructor(Context::class.java)
                            val page = constructor.newInstance(this) as BasePageActivity
                            page.id = View.generateViewId()
                            onStartPageActivity(pageFrom,PageHead(page),intent,requestCode)
                        }
                    }
                }
            }
        }
    }

    private fun newPageActivity(pageHead: PageHead):BasePageActivity{
        val constructor = pageHead.clazz.getConstructor(Context::class.java)
        val page = constructor.newInstance(this) as BasePageActivity
        page.id = pageHead.id
        return page
    }

    /**
     * 保存被回收的PageActivity
     */
    internal fun savePageActivity(pageActivity: BasePageActivity,outState:Bundle){
        synchronized(pageHeadStack){
            val pageIndex = pageHeadStack.find {
                it.id == pageActivity.id
            }
            if(pageIndex!=null){
                pageIndex.savedInstanceState = outState
            }
        }
    }


    private fun onResumePageActivity(pageFrom: BasePageActivity?, pageActivity: BasePageActivity, intent: Intent, requestCode: Int){
        log("[onResumePageActivity]")
//        pageActivity.setWhereFromKey(pageFrom?.id?:-1)
        pageActivity.setRequestCode(requestCode)
        if(pageFrom!=null&&pageFrom.getRequestCode()>0){
            pageActivity.dispatchActivityResult(pageFrom.getRequestCode(),pageFrom.getResultCode(),pageFrom.getResultIntent())
        }
        pageActivity.onPreEnterResumeAnim {
            pageActivity.performResume()
            if(hasWindowFocus()){
                pageActivity.onWindowFocusChanged(true)
            }
        }
    }

    private fun onStartNewIntent(pageFrom: BasePageActivity?, pageHead: PageHead, intent: Intent, requestCode: Int){
        val pageActivity = pageHead.activity
        if(pageActivity==null){
            log("[onStartNewIntent]pageActivity==null")
            //activity被回收，重新启动
            val activity = newPageActivity(pageHead)
            pageHead.activity = activity
            onStartPageActivity(pageFrom,pageHead,intent, requestCode)
        }else{
            log("[onStartNewIntent]")
            pageActivity.setWhereFromKey(pageFrom?.id?:-1)
            pageActivity.setRequestCode(requestCode)
            pageActivity.performNewIntent(intent)
        }
    }

    private fun onStartPageActivity(pageFrom:BasePageActivity?, pageHead: PageHead, intent: Intent, requestCode: Int){
        log("[onStartPageActivity]")
        pageFrom?.onWindowFocusChanged(false)
        pageFrom?.onPreExitPauseAnim {
            pageFrom.performPause()
            pageFrom.performStop()
        }


        val pageActivity = pageHead.activity
        if(pageActivity!=null){
            pageActivity.setWhereFromKey(pageFrom?.id?:-1)
            pageActivity.setRequestCode(requestCode)
            pageActivity.setIntent(intent)
            pageActivity.performCreate(pageHead.savedInstanceState)
            pageActivity.performStart()
            if(pageHead.savedInstanceState!=null){
                pageActivity.dispatchRestoreInstanceState(pageHead.savedInstanceState)
                pageHead.savedInstanceState = null
            }
            if(pageFrom!=null&&pageFrom.getRequestCode()>0){
                pageActivity.dispatchActivityResult(pageFrom.getRequestCode(),pageFrom.getResultCode(),pageFrom.getResultIntent())
            }
            if(pageHeadStack.isNotEmpty()){
                pageActivity.onPreEnterStartAnim {
                    pageActivity.performResume()

                }
            }

            pageHeadStack.push(PageHead(pageActivity))
            if(mDockerLayout.contains(pageActivity)){
                mDockerLayout.removeView(pageActivity)
            }
            mDockerLayout.addView(pageActivity)
        }
    }

    internal fun finishPageActivity(pageActivity: BasePageActivity){
        pageActivity.onWindowFocusChanged(false)
        pageActivity.performPause()
        val pageFrom = findPageActivity {
            it.id==pageActivity.getWhereFromKey()
        }
        //finish之后启动上一个activity
        //这里activity有可能被销毁了，所以先创建一个
        if(pageFrom!=null){
            val intent = Intent()
            val activity = pageFrom.activity
            if(activity==null){
                pageFrom.activity = newPageActivity(pageFrom)
                onStartPageActivity(pageActivity,pageFrom,intent,pageActivity.getRequestCode())
            }else{
                onResumePageActivity(pageActivity,activity,intent,pageActivity.getRequestCode())
            }
        }
        pageHeadStack.removeAll {
            it.id==pageActivity.id
        }
        if(pageHeadStack.isEmpty()){
            finish()
        }else{
            pageActivity.onPreExitFinishAnim {
                pageActivity.performStop()
                mDockerLayout.removeView(pageActivity)
            }
        }
    }

    override fun finish() {
        if(pageHeadStack.size>1){
            finishAllPage()
        }
        super.finish()
    }

    private fun finishAllPage(){
        foreachPageActivity {
            it.finish()
        }
    }

    /**
     * 回收一些Page
     */
    private fun releaseSomePages(){
        val runtime = Runtime.getRuntime()
        val dalvikMax = runtime.maxMemory()
        val dalvikUsed = runtime.totalMemory() - runtime.freeMemory()
        if (dalvikUsed > ((3*dalvikMax)/4)) {
            val index = pageHeadStack[0]
            log("[releaseSomePages]:${index.clazz.simpleName}")
            val activity = index.activity
            if(activity!=null){
                index.savedInstanceState = activity.dispatchSaveInstanceState()
                mDockerLayout.removeView(activity)
            }
            index.activity = null
        }
    }

    private fun log(msg:String){
        LogUtil.i("[${TAG}]:$msg")
    }

}