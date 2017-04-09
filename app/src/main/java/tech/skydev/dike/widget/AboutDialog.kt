package tech.skydev.dike.widget

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import tech.skydev.dike.R

/**
 * Created by Hash Skyd on 4/8/2017.
 */

class AboutDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)

        val inflater = activity.layoutInflater



        builder.setTitle(R.string.action_about)
        builder.setView(inflater.inflate(R.layout.dialog_about, null))
        builder.setPositiveButton("Fermer", null)

        val alert = builder.create()

        return alert
    }
}
