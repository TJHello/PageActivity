package com.njxing.page.info

import android.content.Context
import android.os.Parcelable
import com.njxing.page.BasePageActivity

class PageHead {

    val clazz: Class<out BasePageActivity>
    val id : Int
    var savedInstanceState : Parcelable?=null
    var activity : BasePageActivity ?= null

    constructor(pageActivity: BasePageActivity){
        clazz = pageActivity::class.java
        id = pageActivity.id
        activity = pageActivity
    }

    constructor(context: Context, id:Int, clazz: Class<out BasePageActivity>){
        this.clazz = clazz
        this.id = id
        val constructor = clazz.getConstructor(Context::class.java)
        val page = constructor.newInstance(context) as BasePageActivity
        page.id = id
        this.activity = page
    }
}