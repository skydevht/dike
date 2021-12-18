package tech.skydev.dike.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * Created by Hash Skyd on 3/26/2017.
 */
object ActivityUtils {

    /**
     * The `fragment` is added to the container view with id `frameId`. The operation is
     * performed by the `fragmentManager`.

     */
    fun addFragmentToActivity(fragmentManager: FragmentManager,
                              fragment: Fragment, frameId: Int, backstack: Boolean, tag:String? = null) {
        checkNotNull(fragmentManager)
        checkNotNull(fragment)
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(frameId, fragment, tag)
        if (backstack) transaction.addToBackStack("main")
        transaction.commit()
    }

}