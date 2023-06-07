package com.tjhello.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tjhello.page.dialog.PageDialog
import java.util.Stack

class PageActivityWindows(private val pageActivity: BasePageActivity):Windows(pageActivity) {

    init {
        View.inflate(pageActivity.context,R.layout.lib_page_activity_windows_layout,pageActivity)
    }

    private val layoutActivity : ViewGroup = getDecorView().findViewById(R.id.__PageActivityLayout)
    private val layoutDialog : ViewGroup = getDecorView().findViewById(R.id.__PageDialogLayout)
    private val mDialogStack = Stack<PageDialog>()

    fun getLayoutActivity():ViewGroup = layoutActivity

    fun getLayoutDialog():ViewGroup = layoutDialog

    fun foreachActivity(function:(activity:PageActivity)->Boolean):Boolean{
        return foreach<PageActivity>(layoutActivity){
            return@foreach function(it)
        }
    }

    fun foreachDialog(function:(dialog:PageDialog)->Boolean):Boolean{
        var result = false
        val count = mDialogStack.count()
        for(i in count-1 downTo 0){
            val dialog = mDialogStack[i]
            if(function(dialog)){
                result = true
                break
            }
        }
        return result
    }

    fun showDialog(dialog: PageDialog){
        val dialogView = dialog.windows.getDecorView()
        dialogView.rootView.setOnClickListener {  }
        layoutDialog.addView(dialogView)
        mDialogStack.push(dialog)
    }

    fun dismissDialog(dialog: PageDialog){
        if(layoutDialog.childCount>0){
            val dialogView = dialog.windows.getDecorView()
            layoutDialog.removeView(dialogView)
            mDialogStack.remove(dialog)
            if(mDialogStack.empty()){
                dialogView.rootView.setOnClickListener(null)
            }

        }
    }

    override fun setContentView(layoutId: Int) {
        val view = LayoutInflater.from(pageActivity.context).inflate(layoutId, null, false)
        layoutActivity.removeAllViews()
        layoutActivity.addView(view)
    }

    override fun setContentView(view: View) {
        layoutActivity.removeAllViews()
        layoutActivity.addView(view)
    }

    private inline fun <reified T> foreach (viewGroup: ViewGroup, function: (t: T) -> Boolean):Boolean{
        var result = false
        val count = viewGroup.childCount
        for(i in count-1 downTo 0){
            val dialog = viewGroup.getChildAt(i)
            if(dialog is T){
                if(function(dialog)){
                    result = true
                    break
                }
            }
        }
        return result
    }
}