package es.ericalfonsoponce.presentation.xml.components

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import androidx.core.graphics.drawable.toDrawable
import es.ericalfonsoponce.presentation.xml.databinding.ItemAlertDialogBinding

fun showSimpleDialog(
    context: Context,
    title: String,
    message: String,
    buttonText: String,
    onClose: () -> Unit = {}
): Dialog {
    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

    val binding = ItemAlertDialogBinding.inflate(LayoutInflater.from(context))
    dialog.setContentView(binding.root)


    binding.dialogTitle.text = title
    binding.dialogMessage.text = message
    binding.dialogButton.text = buttonText

    binding.dialogButton.setOnClickListener {
        onClose()
        dialog.dismiss()
    }

    dialog.window?.setGravity(Gravity.CENTER)
    dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())


    dialog.setCanceledOnTouchOutside(false)
    dialog.setCancelable(false)
    dialog.show()
    return dialog
}