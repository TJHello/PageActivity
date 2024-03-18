package com.njxing.page

import android.util.Log

internal object LogUtil {

    private const val TAG = "PageActivityLog"
    var isDebug = false

    fun i(log:String){
        if(isDebug){
            Log.i("PageActivity",log)
        }
    }

    fun e(log:String){
        if(isDebug){
            Log.e("PageActivity",log)
        }
    }

    fun i(name:String,log:()->String){
        if(isDebug){
            Log.i(TAG,"【$name】${log()}")
        }
    }

    fun e(name:String,log:()->String){
        if(isDebug){
            Log.e(TAG,"【$name】${log()}")
        }
    }

}