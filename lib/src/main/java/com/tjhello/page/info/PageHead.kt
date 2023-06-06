package com.tjhello.page.info

import android.os.Parcelable
import com.tjhello.page.BasePageActivity

class PageHead(pageActivity: BasePageActivity) {

    val clazz: Class<out BasePageActivity>
    val id : Int
    var savedInstanceState : Parcelable?=null
    var activity : BasePageActivity ?= null

    init {
        clazz = pageActivity::class.java
        id = pageActivity.id
        activity = pageActivity
    }
}