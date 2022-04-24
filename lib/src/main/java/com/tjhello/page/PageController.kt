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



    internal fun bindDocker(pageDocker: PageDocker){
        if(!mPageDockerList.contains(pageDocker)){
            mPageDockerList.add(pageDocker)
        }
    }

    internal  fun unBindDocker(pageDocker: PageDocker){
        mPageDockerList.remove(pageDocker)
    }




}