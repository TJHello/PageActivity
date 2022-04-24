package com.tjhello.page

import android.content.Context
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

    abstract fun onGetHomePage():Class<out PageActivity>

    private lateinit var mDockerLayout : FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PageController.bindDocker(this)
        injectRootLayout()
        onCreate()
        startPageActivity(null,Intent(this,onGetHomePage()))
    }

    override fun onDestroy() {
        super.onDestroy()
        PageController.unBindDocker(this)
    }

    override fun onBackPressed() {
        val pageActivity = getTopPageActivity()
        pageActivity?.onBackPressed()?:super.onBackPressed()
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        val pageActivity = getTopPageActivity()
        pageActivity?.onUserLeaveHint()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        foreachPageActivity{
            it.onTrimMemory(level)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun injectRootLayout(){
        mDockerLayout = FrameLayout(this)
        this.window.addContentView(mDockerLayout,
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT))
    }

    private fun getTopPageActivity():PageActivity?{
        return if(mDockerLayout.childCount>0){
            mDockerLayout.getChildAt(mDockerLayout.childCount-1) as PageActivity
        }else{
            null
        }
    }

    private fun getPageActivity(clazz: Class<*>):PageActivity?{
        return findPageActivity{
            it::class.java.name==clazz.name
        }
    }

    private fun findPageActivity(function:(PageActivity)->Boolean):PageActivity?{
        for (i in  mDockerLayout.childCount-1 downTo 0){
            val pageActivity = mDockerLayout.getChildAt(i) as PageActivity
            if(function(pageActivity)){
                return pageActivity
            }
        }
        return null
    }

    private fun foreachPageActivity(function:(PageActivity)->Unit){
        for (i in  mDockerLayout.childCount-1 downTo 0){
            val pageActivity = mDockerLayout.getChildAt(i) as PageActivity
            function(pageActivity)
        }
    }

    internal fun startPageActivity(pageFrom:PageActivity?,intent: Intent,requestCode: Int=0){
        val component = intent.component
        if(component!=null){
            val className = component.className
            val clazz = Class.forName(className)
            if(PageActivity::class.java.isAssignableFrom(clazz)){
                when(intent.flags){
                    //单例模式
                    Intent.FLAG_ACTIVITY_NEW_TASK->{
                        val pageActivity = this.getPageActivity(clazz)
                        if(pageActivity!=null&&pageActivity::class.java.name==className){
                            onStartNewIntent(pageFrom,pageActivity,intent,requestCode)
                        }else{
                            val constructor = clazz.getConstructor(Context::class.java)
                            val page = constructor.newInstance(this) as PageActivity
                            this.onStartPageActivity(pageFrom,page,intent,requestCode)
                        }
                    }
                    //断背模式
                    Intent.FLAG_ACTIVITY_CLEAR_TOP->{
                        val pageActivity = this.getPageActivity(clazz)
                        if(pageActivity!=null&&pageActivity::class.java.name==className){
                            for (i in  mDockerLayout.childCount-1 downTo 0){
                                val page = mDockerLayout.getChildAt(i) as PageActivity
                                if(page==pageActivity){
                                    finishPageActivity(page)
                                    break
                                }
                                finishPageActivity(page)
                            }
                            onStartNewIntent(pageFrom,pageActivity,intent,requestCode)
                        }else{
                            val constructor = clazz.getConstructor(Context::class.java)
                            val page = constructor.newInstance(this) as PageActivity
                            onStartPageActivity(pageFrom,page,intent,requestCode)
                        }
                    }
                    //断背+复用模式
                    Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_SINGLE_TOP->{
                        val pageActivity = this.getPageActivity(clazz)
                        if(pageActivity!=null&&pageActivity::class.java.name==className){
                            for (i in  mDockerLayout.childCount-1 downTo 0){
                                val page = mDockerLayout.getChildAt(i) as PageActivity
                                if(page==pageActivity){
                                    break
                                }
                                finishPageActivity(page)
                            }
                            onStartNewIntent(pageFrom,pageActivity, intent,requestCode)
                        }else{
                            val constructor = clazz.getConstructor(Context::class.java)
                            val page = constructor.newInstance(this) as PageActivity
                            onStartPageActivity(pageFrom,page,intent,requestCode)
                        }
                    }
                    //栈顶模式
                    0 or Intent.FLAG_ACTIVITY_SINGLE_TOP->{
                        val pageActivity = getTopPageActivity()
                        if(pageActivity!=null&&pageActivity::class.java.name==className){
                            onStartNewIntent(pageFrom,pageActivity,intent,requestCode)
                        }else{
                            val constructor = clazz.getConstructor(Context::class.java)
                            val page = constructor.newInstance(this) as PageActivity
                            onStartPageActivity(pageFrom,page,intent,requestCode)
                        }
                    }
                }
            }
        }
    }

    private fun onStartNewIntent(pageFrom: PageActivity?, pageActivity: PageActivity, intent: Intent, requestCode: Int){
        pageActivity.setWhereFromKey(pageFrom?.id?:0)
        pageActivity.setRequestCode(requestCode)
        pageActivity.onNewIntent(intent)

    }

    private fun onStartPageActivity(pageFrom:PageActivity?,pageActivity: PageActivity, intent: Intent,requestCode: Int){
        pageActivity.setWhereFromKey(pageFrom?.id?:0)
        pageActivity.setRequestCode(requestCode)
        pageActivity.setIntent(intent)
        pageActivity.onCreate(null)
        pageActivity.onPostCreate()
        pageActivity.onStart()
        pageActivity.onResume()
        pageActivity.onPostResume()
        mDockerLayout.addView(pageActivity)
    }

    internal fun finishPageActivity(pageActivity: PageActivity){
        pageActivity.onPause()
        if(pageActivity.getRequestCode()!=0){
            val pageFrom = findPageActivity {
                it.id==pageActivity.id
            }
            pageFrom?.onActivityResult(pageActivity.getRequestCode(),pageActivity.getResultCode(),pageActivity.getResultIntent())
        }
        mDockerLayout.removeView(pageActivity)
        pageActivity.onStop()

    }

    override fun finish() {
        finishAllPage()
        super.finish()
    }

    private fun finishAllPage(){
        foreachPageActivity {
            finishAllPage()
        }
    }

}