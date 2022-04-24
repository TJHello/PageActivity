package com.tjhello.demo.page

import com.tjhello.page.PageActivity
import com.tjhello.page.PageDocker

class MainActivity : PageDocker() {
    override fun onCreate() {
    }

    override fun onGetHomePage(): Class<out PageActivity> {
        return LauncherPageActivity::class.java
    }
}