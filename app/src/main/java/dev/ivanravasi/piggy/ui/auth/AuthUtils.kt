package dev.ivanravasi.piggy.ui.auth

import android.os.Build
import android.text.SpannableString
import android.text.style.UnderlineSpan

object ViewUtils {
    fun underlineText(text: CharSequence): SpannableString {
        val underlinedText = SpannableString(text)
        underlinedText.setSpan(UnderlineSpan(), 0, underlinedText.length, 0)
        return underlinedText
    }
}

object ModelUtils {
    fun deviceName(): String = "${Build.MANUFACTURER} ${Build.MODEL}"
}