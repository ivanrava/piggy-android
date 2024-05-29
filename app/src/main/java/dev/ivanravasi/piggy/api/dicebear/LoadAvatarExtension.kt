package dev.ivanravasi.piggy.api.dicebear

import android.widget.ImageView
import coil.decode.SvgDecoder
import coil.load
import dev.ivanravasi.piggy.R

fun ImageView.loadAvatar(
    style: String,
    seed: String
) {
    val backgroundColor = "b6e3f4,c0aede,d1d4f9,ffd5dc,ffdfbf"
    val shapeColor = "0a5b83,1c799f,69d2e7,f1f4dc,f88c49"
    this.load("https://api.dicebear.com/8.x/$style/svg?seed=$seed&flip=false&rotate=0&scale=85&radius=0&size=96&backgroundColor=$backgroundColor&shapeColor=$shapeColor") {
        size(128)
        decoderFactory { result, options, _ -> SvgDecoder(result.source, options) }
        // TODO: set a better placeholder
//        placeholder(R.drawable.ic_properties_24)
        crossfade(true)
    }
}

fun ImageView.loadFallback(
    seed: String
) {
    this.load("https://api.dicebear.com/8.x/initials/svg?seed=$seed&flip=false&rotate=0&scale=100&radius=0&size=96&fontWeight=600") {
        size(128)
        decoderFactory { result, options, _ -> SvgDecoder(result.source, options) }
        // TODO: set a better placeholder
//        placeholder(R.drawable.ic_properties_24)
        crossfade(true)
    }
}
