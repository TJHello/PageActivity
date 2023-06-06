package com.tjhello.demo.page

import android.app.Application
import com.eyewind.lib.log.EyewindLog

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        EyewindLog.setDebug(true)
    }
}