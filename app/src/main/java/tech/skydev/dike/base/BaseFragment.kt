package tech.skydev.dike.base

import android.content.res.Configuration
import android.support.v4.app.Fragment

/**
 * Created by Hash Skyd on 3/28/2017.
 */

abstract class BaseFragment : Fragment() {;

    val screenOrientation: Int
        get() {
            val getOrient = activity.windowManager.defaultDisplay
            var orientation = Configuration.ORIENTATION_UNDEFINED
            if (getOrient.width == getOrient.height) {
                orientation = Configuration.ORIENTATION_SQUARE
            } else {
                if (getOrient.width < getOrient.height) {
                    orientation = Configuration.ORIENTATION_PORTRAIT
                } else {
                    orientation = Configuration.ORIENTATION_LANDSCAPE
                }
            }
            return orientation
        }
}
