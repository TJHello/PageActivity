package com.njxing.demo.page

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewPropertyAnimator
import android.widget.Button
import android.widget.TextView
import com.njxing.demo.page.databinding.SubPageActivityLayoutBinding
import com.njxing.page.PageActivity

class SubPageActivity(context: Context) : PageActivity(context) {

    private val mViewBinding by lazy { SubPageActivityLayoutBinding.bind(this.getPageWindows().getLayoutActivity()) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.sub_page_activity_layout)

        mViewBinding.btBack.setOnClickListener {
            this.setResult(RESULT_OK, Intent().apply {
                this.data = Uri.parse("https://www.hao123.com")
            })
            this.finish()
        }
        mViewBinding.btNewPage.setOnClickListener {
            startActivity(SubPageActivity::class.java)
        }
    }

    override fun onPreEnterStartAnim(animId: Int, function: () -> Unit) {
        scaleX = 0.8f
        scaleY = 0.8f
        translationX = getScreenWidth().toFloat()
        animate().scaleX(1f).scaleY(1f).translationX(0f).onEnd{
            function()
        }
    }

    override fun onPreExitFinishAnim(function: () -> Unit) {
        animate().scaleX(0.8f).scaleY(0.8f).translationX(getScreenWidth().toFloat()).onEnd{
            function()
        }
    }

    private fun ViewPropertyAnimator.onEnd(function:()->Unit): ViewPropertyAnimator {
        this.setListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                function()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

        })
        return this
    }
}