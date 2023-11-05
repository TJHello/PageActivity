package com.njxing.page

import android.util.Log

internal object LogUtil {

    fun i(log:String){
        Log.i("PageActivity",log)
    }

    fun e(log:String){
        Log.e("PageActivity",log)
    }

}