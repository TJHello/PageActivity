package com.tjhello.demo.page

import android.content.Intent
import com.tjhello.page.PageController
import com.tjhello.page.PageDocker

class MainActivity : PageDocker() {
    override fun onCreate() {
        PageController.startPageActivity(this,Intent(this,LauncherPageActivity::class.java))
    }
}