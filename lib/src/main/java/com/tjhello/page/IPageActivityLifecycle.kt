package com.tjhello.page

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable

interface IPageActivityLifecycle {

    fun performCreate(savedInstanceState: Parcelable?)

    fun performNewIntent(intent: Intent)

    fun performStart()

    fun performRestart()

    fun performResume()

    fun performPause()

    fun performUserLeaving()

    fun performStop()

    fun performDestroy()

    fun dispatchActivityResult(requestCode:Int, resultCode:Int,data:Intent?)

    fun dispatchRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)

    fun dispatchSaveInstanceState(): Parcelable?

    fun dispatchRestoreInstanceState(state: Parcelable?)

    fun performBackPressed()
}