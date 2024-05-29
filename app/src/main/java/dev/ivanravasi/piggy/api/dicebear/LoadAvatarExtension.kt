package dev.ivanravasi.piggy.api.dicebear

import android.widget.ImageView
import coil.decode.SvgDecoder
import coil.load
import dev.ivanravasi.piggy.R

fun ImageView.loadAvatar(
    style: String,
    seed: String
) {
    this.load("https://api.dicebear.com/8.x/$style/svg?seed=$seed") {
        size(128)
        decoderFactory { result, options, _ -> SvgDecoder(result.source, options) }
        // TODO: set a better placeholder
//        placeholder(R.drawable.ic_properties_24)
        crossfade(true)
    }
}
