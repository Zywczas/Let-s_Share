package com.zywczas.letsshare.utils

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputLayout.END_ICON_PASSWORD_TOGGLE
import com.zywczas.letsshare.R

@BindingAdapter("isVisible")
fun bindIsVisible(view: View, isVisible: Boolean){
    view.isVisible = isVisible
}

@BindingAdapter("setPasswordEndingIconTint")
fun bindSetPasswordEndingIconTint(view: View, setTint: Boolean){
    if (setTint && view is TextInputLayout && view.endIconMode == END_ICON_PASSWORD_TOGGLE){
        view.editText?.setOnFocusChangeListener { _, hasFocus ->
            val color = if (hasFocus) {
                ContextCompat.getColor(view.context, R.color.myColorPrimaryVariant)
            } else { Color.GRAY }
            view.setEndIconTintList(ColorStateList.valueOf(color))
        }
    }
}