package tech.skydev.dike.widget

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import tech.skydev.dike.R

/**
 * Created by Hash Skyd on 4/8/2017.
 */

class AboutDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(context!!)

        val inflater = activity!!.layoutInflater



        builder.setTitle(R.string.action_about)
        builder.setView(inflater.inflate(R.layout.dialog_about, null))
        builder.setPositiveButton("Fermer", null)

        val alert = builder.create()

        return alert
    }
}
