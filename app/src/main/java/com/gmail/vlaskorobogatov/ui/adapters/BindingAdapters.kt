package com.gmail.vlaskorobogatov.ui.adapters

import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.graphics.toColorInt
import androidx.databinding.BindingAdapter

@BindingAdapter("tint")
fun ImageButton.setImageTint(color: String) {
    setColorFilter(color.toColorInt())
}

@BindingAdapter("cardBackgroundColor")
fun CardView.setBackgroundColor(color: String) {
    setCardBackgroundColor(color.toColorInt())
}

@BindingAdapter("android:backgroundTint")
fun Button.setBackgroundColor(color: String) {
    setBackgroundColor(color.toColorInt())
}

@BindingAdapter("android:textColor")
fun Button.setTextColor(color: String) {
    setTextColor(color.toColorInt())
}

@BindingAdapter("android:textColor")
fun TextView.setTextColor(color: String) {
    setTextColor(color.toColorInt())
}