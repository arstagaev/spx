package com.revolve44.solarpanelx.domain.core

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.widget.ImageView
import com.revolve44.solarpanelx.global_utils.ConstantsCalculations.Companion.is_LIGHT_MODE
import timber.log.Timber


// for blinked of Text Color
@SuppressLint("ObjectAnimatorBinding")
fun blinkATextView(uiElement: Any, color1: Int, color2: Int,color3: Int, duration: Int){
    if (!is_LIGHT_MODE){
        val skyAnim: ValueAnimator = ObjectAnimator.ofInt(uiElement, "textColor",
            (color1),
            (color2),
            (color3))

        skyAnim.duration = duration.toLong()
        skyAnim.setEvaluator(ArgbEvaluator())
        skyAnim.start()
    }


}

/** smooth gradient animation for background
 * [1,2 - nedded in all cases, 3dr color is option (may equal him to zero)]  */

@SuppressLint("ObjectAnimatorBinding")
fun gradientAnimation(uiElement: Any, color1: Int, color2: Int,color3: Int,color4: Int, color5 : Int, duration: Int){
    if (!is_LIGHT_MODE){
        val colorAnimation1: ValueAnimator = ObjectAnimator.ofInt(uiElement, "backgroundColor",
            color1, color2, color3, color4, color5)

        colorAnimation1.duration = duration.toLong()
        colorAnimation1.setEvaluator(ArgbEvaluator())
        colorAnimation1.start()
    }

}

@SuppressLint("ObjectAnimatorBinding")
fun gradientAnimationLayout(uiElement: Any, color1: Int, color2: Int, duration: Int){
    Timber.w("ui is Ligthmode:${is_LIGHT_MODE}")
    if (!is_LIGHT_MODE){

        val colorAnimation1: ValueAnimator = ObjectAnimator.ofInt(uiElement, "backgroundColor",
            color1, color2)

        colorAnimation1.duration = duration.toLong()
        colorAnimation1.setEvaluator(ArgbEvaluator())
        colorAnimation1.start()
    }

}

@SuppressLint("ObjectAnimatorBinding")
fun gradientAnimation2(uiElement: ImageView, color1: Int, color2: Int, duration: Int){

    val colorAnimation2: ValueAnimator = ValueAnimator.ofObject(ArgbEvaluator(),
        color1,
        color2)

    colorAnimation2.duration = duration.toLong()
    //skyAnim3.setEvaluator(ArgbEvaluator())
    colorAnimation2.addUpdateListener { uiElement.setColorFilter(it.getAnimatedValue() as Int) }
    colorAnimation2.start()
}

fun randomName() = listOf(
    "not tired?:)", "are you kidding?", "OK, I get it.", "all will be alright", "now i am tired"
).random()

fun listOfColor(num: Int) = listOf(
    Color.WHITE, Color.BLUE, Color.GREEN, Color.CYAN, Color.MAGENTA, Color.YELLOW, Color.RED
).get(num)