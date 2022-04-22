package com.tjhello.demo.page

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.tjhello.page.PageActivity

class Test1PageActivity(context: Context) : PageActivity(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.test_page_activity_layout)

        this.findViewById<Button>(R.id.btBack).setOnClickListener {
            this.finish()
        }
        this.findViewById<TextView>(R.id.tvTitle).text = getIntent().getStringExtra("title")

    }

}