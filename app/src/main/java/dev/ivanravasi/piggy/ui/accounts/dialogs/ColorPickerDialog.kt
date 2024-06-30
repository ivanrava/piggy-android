package dev.ivanravasi.piggy.ui.accounts.dialogs

import android.content.Context
import android.view.LayoutInflater
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.skydoves.colorpickerview.ColorEnvelope
import dev.ivanravasi.piggy.databinding.DialogColorPickerBinding

class ColorPickerDialog {
    fun show(context: Context, onColorSelected: (color: ColorEnvelope) -> Unit) {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = DialogColorPickerBinding.inflate(layoutInflater)
        binding.colorPickerView.attachBrightnessSlider(binding.brightnessSlide)

        MaterialAlertDialogBuilder(context)
            .setTitle("Select account color")
            .setView(binding.root)
            .setNeutralButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Select") { dialog, which ->
                onColorSelected(binding.colorPickerView.colorEnvelope)
                dialog.dismiss()
            }
            .show()
    }
}