package com.tjhello.demo.page

import android.annotation.SuppressLint
import com.tjhello.page.BasePageActivity
import com.tjhello.page.dialog.PageDialog

@SuppressLint("ViewConstructor")
class HomeDialog(mPageActivity: BasePageActivity) : PageDialog(mPageActivity) {

    override fun onCreate() {
        setContentView(R.layout.home_dialog_layout)
        listenerClick(R.id.btOk)
    }

}