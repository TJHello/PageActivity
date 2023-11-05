package com.njxing.demo.page

import android.content.Intent
import android.os.Bundle
import com.njxing.page.PageActivity
import com.njxing.page.PageDocker

class MainActivity : PageDocker() {

    override fun onGetHomePage(): Class<out PageActivity> {
        return LauncherPageActivity::class.java
    }

    override fun onPreInjectRootLayout() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

}