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

@BindingAdapter("android:weatherIconDescription")
fun setWeatherIconDescription(imageView: ImageView, forecastIcon: String) {
    imageView.contentDescription = when(forecastIcon) {
        "01n" -> "Clear sky"
        "02n" -> "Few clouds"
        "03n" -> "Scattered clouds"
        "04n" -> "Broken clouds"
        "09n" -> "Shower rain"
        "10n" -> "Rain"
        "11n" -> "Thunderstorm"
        "13n" -> "Snow"
        "50n" -> "Mist"
        else -> "Weather unknown"
    }
}