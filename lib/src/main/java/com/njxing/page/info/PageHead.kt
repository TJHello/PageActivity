package com.njxing.page.info

import android.os.Parcelable
import com.njxing.page.BasePageActivity

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