package com.njxing.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import com.njxing.page.dialog.PageDialog
import java.util.Stack

class PageActivityWindows(val pageActivity: BasePageActivity):Windows(pageActivity) {

    private val layoutActivity : ViewGroup = pageActivity.findViewById(R.id.__PageActivityLayout)
    private val layoutDialog : ViewGroup = pageActivity.findViewById(R.id.__PageDialogLayout)
    private val mDialogStack = Stack<PageDialog>()

    fun getLayoutActivity():ViewGroup = layoutActivity[0] as ViewGroup

    fun getLayoutDialog():ViewGroup = layoutDialog[0] as ViewGroup

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
        val dialogView = dialog.windows
        dialogView.rootView.setOnClickListener {  }
        LogUtil.i("PageActivityWindows"){
            "showDialog:${layoutDialog.childCount}"
        }
        layoutDialog.addView(dialogView)
        mDialogStack.push(dialog)
    }

    fun dismissDialog(dialog: PageDialog){
        if(layoutDialog.childCount>0){
            val dialogView = dialog.windows
            layoutDialog.removeView(dialogView)
            mDialogStack.remove(dialog)
            if(mDialogStack.empty()){
                dialogView.rootView.setOnClickListener(null)
            }

        }
    }

    override fun setContentView(layoutId: Int) {
        val view = LayoutInflater.from(pageActivity.context).inflate(layoutId, null)
        layoutActivity.removeAllViews()
        layoutActivity.addView(view,LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT))
    }

    override fun setContentView(view: View) {
        layoutActivity.removeAllViews()
        layoutActivity.addView(view,LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT))
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