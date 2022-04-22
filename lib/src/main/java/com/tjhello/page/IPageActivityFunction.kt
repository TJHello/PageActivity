package com.tjhello.page

import android.content.Intent


interface IPageActivityFunction {

    //长按Home键或者点击Home键的时候触发
    fun onUserLeaveHint()

    //内存状态回调
    fun onTrimMemory(level:Int)

    //二次启动Activity的时候
    fun onNewIntent(intent: Intent)

    fun onRequestPermissionsResult(
        requestCode:Int,
        permissions:Array<out String>,
        grantResults:IntArray
    )

    fun onBackPressed()

}