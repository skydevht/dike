package tech.skydev.dike

import android.content.Context
import android.widget.Toast

/**
 * Created by Hash Skyd on 3/26/2017.
 */
interface BaseView<T> {
    var context2: Context
    fun setPresenter(presenter: T)
    fun showMessage(message: String) {
       Toast.makeText(context2, message, Toast.LENGTH_SHORT).show()
    }
}