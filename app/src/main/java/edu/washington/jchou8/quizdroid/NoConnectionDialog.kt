package edu.washington.jchou8.quizdroid

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog

class NoConnectionDialog : DialogFragment() {
    internal lateinit var listener: NoConnectionDialogListener
    interface NoConnectionDialogListener {
        fun onDialogNegativeClick(dialog: DialogFragment)
        fun onDialogPositiveClick(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as NoConnectionDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() +
                    " must implement NoticeDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Device is currently offline.")
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        listener.onDialogNegativeClick(this)
                    })
                .setPositiveButton("Retry",
                    DialogInterface.OnClickListener { dialog, id ->
                        listener.onDialogPositiveClick(this)
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}