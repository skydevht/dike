package tech.skydev.dike.util

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.widget.TextView
import tech.skydev.dike.R

/**
 * Created by Hash Skyd on 3/29/2017.
 */
object ViewAnimationUtils {

    fun changeCellColor(cell: View, darken: Boolean, animationListener: Animator.AnimatorListener? = null) {
        val context = cell.context
        val animate: ObjectAnimator = if (darken)
            AnimatorInflater.loadAnimator(context, R.animator.cell_light_to_dark) as ObjectAnimator
        else
            AnimatorInflater.loadAnimator(context, R.animator.cell_dark_to_light) as ObjectAnimator

        animate.setTarget(cell)
        val textAnimate: ObjectAnimator = if (darken)
            AnimatorInflater.loadAnimator(context, R.animator.text_dark_to_light) as ObjectAnimator
        else
            AnimatorInflater.loadAnimator(context, R.animator.text_light_to_dark) as ObjectAnimator
        val textView = cell.findViewById(R.id.name) as TextView
        textAnimate.setTarget(textView)
        val viewAnimate: AnimatorSet = AnimatorSet()
        animationListener?.let {
            viewAnimate.addListener(animationListener)
        }
        viewAnimate.playTogether(animate, textAnimate)
        viewAnimate.start()
    }
}