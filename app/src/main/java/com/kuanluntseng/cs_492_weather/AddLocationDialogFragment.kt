package com.kuanluntseng.cs_492_weather

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import kotlinx.coroutines.NonCancellable.cancel

class AddLocationDialogFragment(private val addLocationDialogListener: AddLocationDialogListener) :
    DialogFragment() {

    interface AddLocationDialogListener {
        fun onDialogPositiveClick(location: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;

            val view = inflater.inflate(R.layout.add_location_dialog, null)
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(view)
                .setTitle("Add Location")
                .setPositiveButton(
                    "OK"
                ) { _, _ ->
                    val editText = view.findViewById<EditText>(R.id.location_edittext)
                    addLocationDialogListener.onDialogPositiveClick(editText.text.toString())
                }
                .setNegativeButton(
                    "CANCEL"
                ) { dialog, _ ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}