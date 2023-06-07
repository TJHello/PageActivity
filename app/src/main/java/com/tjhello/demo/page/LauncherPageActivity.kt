package com.tjhello.demo.page

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.widget.Button
import com.eyewind.lib.log.EyewindLog
import com.tjhello.page.PageActivity

class LauncherPageActivity(context: Context) : PageActivity(context) {

    private var num = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layoutcher_page_activity_layout)
        val value = savedInstanceState?.getString("key")
        EyewindLog.i("[onCreate]:$value")
        this.findViewById<Button>(R.id.btNext).setOnClickListener {
            val intent = Intent(context,Test1PageActivity::class.java)
            intent.putExtra("title","标题${++num}")
            startActivityForResult(intent,1)
        }
        this.findViewById<Button>(R.id.btShowDialog).setOnClickListener {
            HomeDialog(this)
                .onShow {
                    EyewindLog.i("onShow")
                }
                .onDismiss {
                    EyewindLog.i("onDismiss")
                }
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("key","value")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val value = savedInstanceState.getString("key")
        EyewindLog.i("[onRestoreInstanceState]:$value")
    }
}