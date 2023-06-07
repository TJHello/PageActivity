package com.tjhello.page.dialog

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.tjhello.page.BasePageActivity
import com.tjhello.page.Windows

abstract class PageDialog(private val pageActivity : BasePageActivity,layoutId:Int=0) {

    private var onShow : ()->Unit = {}
    private var onDismiss : ()->Unit = {}
    private var onClick : (v:View)->Unit = {}
    private val context = pageActivity.context
    val windows by lazy { Windows(PageDialogView(context)) }
    private var isShowed = false
    private var isDismissed = false


    init {
        if(layoutId>0){
            windows.setContentView(layoutId)
        }
        this.onCreate()
    }

    open fun onCreate(){

    }

    open fun show(){
        if(!isShowed){
            isShowed = true
            windows.getDecorView().setOnClickListener {}
            pageActivity.windows.showDialog(this)
            onShow()
        }
    }

    open fun dismiss(){
        if(!isDismissed){
            isDismissed = true
            pageActivity.windows.dismissDialog(this)
            onDismiss()
        }
    }

    fun setContentView(layoutId: Int){
        windows.setContentView(layoutId)
    }

    fun setContentView(view:View){
        windows.setContentView(view)
    }

    fun listenerClick(id:Int){
        this.findViewById<View>(id)?.setOnClickListener {
            onClick(it)
        }
    }

    fun onShow(function:()->Unit):PageDialog{
        onShow = function
        return this
    }

    fun onDismiss(function:()->Unit):PageDialog{
        onDismiss = function
        return this
    }

    fun onClick(function:(v:View)->Unit):PageDialog{
        onClick = function
        return this
    }

    open fun onBackPressed():Boolean{
        dismiss()
        return true
    }

    protected fun <T : View> findViewById(id:Int):T?{
        return windows.getDecorView().findViewById(id)
    }

    fun getContext():Context = context


    class PageDialogView(context: Context, attrs: AttributeSet?=null, defStyleAttr: Int=0) : FrameLayout(context, attrs, defStyleAttr) {

    }
}