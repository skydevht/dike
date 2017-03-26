package tech.skydev.dike.util

import android.content.Context
import android.util.DisplayMetrics

/**
 * Created by Hash Skyd on 3/26/2017.
 */

object ConversionUtil {
    fun dpToPx(context: Context, dp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }
}
