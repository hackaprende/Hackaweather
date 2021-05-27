package com.hackaprende.hackaweather.common

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.hackaprende.hackaweather.R

@BindingAdapter("android:tempText")
fun setTempText(textView: TextView, temperature: Double) {
    val context = textView.context

    textView.text = context.getString(R.string.temp_format, temperature)
}

@BindingAdapter("android:glideImage")
fun setGlideImage(imageView: ImageView, iconId: String) {
    val context = imageView.context
    val url = context.getString(R.string.icon_url_format, iconId)

    Glide.with(context)
        .load(url.replace("http", "https"))
        .into(imageView)
}