package edu.washington.jchou8.quizdroid

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog

class AirplaneModeDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Airplane mode is currently enabled. Would you like to disable it?")
                .setNegativeButton("No",
                    DialogInterface.OnClickListener { dialog, id ->

                    })
                .setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, id ->
                        val settingsIntent = Intent(android.provider.Settings.ACTION_AIRPLANE_MODE_SETTINGS)
                        startActivity(settingsIntent)
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}