package com.njxing.demo.page

import android.annotation.SuppressLint
import com.njxing.page.BasePageActivity
import com.njxing.page.dialog.PageDialog

@SuppressLint("ViewConstructor")
class HomeDialog(mPageActivity: BasePageActivity) : PageDialog(mPageActivity) {

    override fun onCreate() {
        setContentView(R.layout.home_dialog_layout)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        listenerClick(R.id.btOk)
        listenerClick(R.id.btCancel)
    }
}