package dev.ivanravasi.piggy.ui.iconify

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.imageview.ShapeableImageView
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.iconify.loadIconify

class IconPickerView(
    context: Context,
    attrs: AttributeSet
): ShapeableImageView(context, attrs) {
    private val iconColor = ContextCompat.getColor(context, R.color.md_theme_onPrimary)
    private val errorColor = ContextCompat.getColor(context, R.color.md_theme_error)
    private val listeners: MutableList<(icon: String) -> Unit> = mutableListOf()

    init {
        DrawableCompat.setTint(drawable, iconColor)
        setOnClickListener {
            imageTintList = null
            getFragmentManager()?.let { fragmentManager ->
                IconPickerBottomSheet(iconColor, object : OnIconClickListener {
                    override fun onIconClick(icon: String) {
                        loadIconify(icon, iconColor)
                        listeners.forEach { it(icon) }
                    }
                }).show(fragmentManager, "IconPicker")
            }
        }
    }

    private fun getFragmentManager(): FragmentManager? {
        return if (context is FragmentActivity)
            (context as FragmentActivity).supportFragmentManager
        else
            null
    }

    fun setError() {
        imageTintList = ColorStateList.valueOf(errorColor)
    }

    public fun setOnSelectedIconListener(listener: (icon: String) -> Unit) {
        listeners.add(listener)
    }

    fun loadIconify(icon: String) {
        loadIconify(icon, iconColor)
    }
}