package com.njxing.demo.page

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.njxing.demo.page.databinding.MainPageActivityLayoutBinding
import com.njxing.page.PageActivity

class MainPageActivity(context: Context) : PageActivity(context) {

    private val mViewBinding by lazy { MainPageActivityLayoutBinding.bind(this.getPageWindows().getLayoutActivity()) }
    private var num = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page_activity_layout)

        mViewBinding.btNext.setOnClickListener {
            val intent = Intent(context,SubPageActivity::class.java)
            intent.putExtra("title","标题${++num}")
            startActivityForResult(intent,1)
        }

        mViewBinding.btShowDialog.setOnClickListener {
            val dialog = HomeDialog(this)
            dialog.onClick {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

}