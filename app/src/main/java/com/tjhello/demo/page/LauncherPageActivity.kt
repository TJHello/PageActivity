package com.tjhello.demo.page

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.tjhello.page.PageActivity

class LauncherPageActivity(context: Context) : PageActivity(context) {

    private var num = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layoutcher_page_activity_layout)

        this.findViewById<Button>(R.id.btNext).setOnClickListener {
            val intent = Intent(context,Test1PageActivity::class.java)
            intent.putExtra("title","标题${++num}")
            startActivity(intent)
        }


    }

}