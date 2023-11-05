package com.njxing.demo.page

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.njxing.page.PageActivity

class Test1PageActivity(context: Context) : PageActivity(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.test_page_activity_layout)

        this.findViewById<Button>(R.id.btBack).setOnClickListener {
            this.setResult(RESULT_OK, Intent().apply {
                this.data = Uri.parse("https://www.hao123.com")
            })
            this.finish()
        }
        this.findViewById<TextView>(R.id.tvTitle).text = getIntent().getStringExtra("title")

    }

}