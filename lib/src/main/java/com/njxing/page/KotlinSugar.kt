package com.njxing.page

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.ViewPropertyAnimator

internal class KotlinSugar {
}

internal fun ViewPropertyAnimator.onEnd(function:()->Unit):ViewPropertyAnimator{
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

internal fun ValueAnimator.onUpdate(function: (ValueAnimator) -> Unit):ValueAnimator{
    this.addUpdateListener {
        function(it)
    }
    return this
}

internal fun Animator.onEnd(function: () -> Unit): Animator{
    this.addListener(object : Animator.AnimatorListener{
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