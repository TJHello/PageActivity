package com.njxing.demo.page

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import com.njxing.page.PageActivity
import com.njxing.page.PageDocker

class MainActivity : PageDocker() {

    override fun onGetHomePage(): Class<out PageActivity> {
        return MainPageActivity::class.java
    }

    override fun onPreInjectRootLayout() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //自定义容器布局
    override fun onCustomDockerLayout(): FrameLayout? {
        return super.onCustomDockerLayout()
    }
}