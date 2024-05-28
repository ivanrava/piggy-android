package dev.ivanravasi.piggy.api.iconify

import android.widget.ImageView
import coil.decode.SvgDecoder
import coil.load
import dev.ivanravasi.piggy.R

fun ImageView.loadIconify(
    icon: String,
    fillColor: Int = 0
) {
    val iconFields = icon.split(":")
    val prefix = iconFields[0]
    val name = iconFields[1]
    val color = String.format("%06X", (0xFFFFFF and fillColor))
    this.load("https://api.iconify.design/$prefix/$name.svg?color=%23$color") {
        size(64)
        decoderFactory { result, options, _ -> SvgDecoder(result.source, options) }
        // TODO: set a better placeholder
        placeholder(R.drawable.ic_properties_24)
        crossfade(true)
    }
}