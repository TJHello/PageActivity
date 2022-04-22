package com.tjhello.page

import android.os.Bundle

interface IPageActivityLifecycle {

    fun onCreate(savedInstanceState:Bundle?)

    fun onResume()

    fun onPause()

    fun onDestroy()

    fun onStart()

    fun onStop()

    fun onPostCreate()

    fun onPostResume()


}