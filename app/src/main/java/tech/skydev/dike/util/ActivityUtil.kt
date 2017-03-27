package tech.skydev.dike.util

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

/**
 * Created by Hash Skyd on 3/26/2017.
 */
object ActivityUtils {

    /**
     * The `fragment` is added to the container view with id `frameId`. The operation is
     * performed by the `fragmentManager`.

     */
    fun addFragmentToActivity(fragmentManager: FragmentManager,
                              fragment: Fragment, frameId: Int, backstack: Boolean) {
        checkNotNull(fragmentManager)
        checkNotNull(fragment)
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(frameId, fragment)
        if (backstack) transaction.addToBackStack("main")
        transaction.commit()
    }

}