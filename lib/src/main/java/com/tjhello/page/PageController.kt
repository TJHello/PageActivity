package com.tjhello.page

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import java.util.*
import kotlin.collections.ArrayList

object PageController {

    private val mPageDockerList = ArrayList<PageDocker>()


    fun newDocker(context:Context, clazz: Class<out PageDocker>){
        val intent = Intent(context,clazz)
        context.startActivity(intent)
    }

    fun startPageActivity(pageDocker: PageDocker,intent: Intent){
        val component = intent.component
        if(component!=null){
            val className = component.className
            val clazz = Class.forName(className)
            if(PageActivity::class.java.isAssignableFrom(clazz)){
                val constructor = clazz.getConstructor(Context::class.java)
                val pageActivity = constructor.newInstance(pageDocker) as PageActivity
                pageDocker.startPageActivity(pageActivity,intent)
            }
        }
    }

    fun finishPageActivity(pageDocker: PageDocker,pageActivity: PageActivity){
        pageDocker.finishPageActivity(pageActivity)
    }

    internal fun bindDocker(pageDocker: PageDocker){
        if(!mPageDockerList.contains(pageDocker)){
            mPageDockerList.add(pageDocker)
        }
    }

    internal  fun unBindDocker(pageDocker: PageDocker){
        mPageDockerList.remove(pageDocker)
    }




}