package com.njxing.demo.page

import android.annotation.SuppressLint
import com.njxing.page.BasePageActivity
import com.njxing.page.dialog.PageDialog

@SuppressLint("ViewConstructor")
class HomeDialog(mPageActivity: BasePageActivity) : PageDialog(mPageActivity) {

    override fun onCreate() {
        setContentView(R.layout.home_dialog_layout)
        listenerClick(R.id.btOk)
    }


    override fun dismiss() {
        super.dismiss()
    }
}