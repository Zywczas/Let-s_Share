package com.zywczas.letsshare.extentions

import android.app.Activity
import android.view.View
import androidx.core.content.ContextCompat
import com.leinardi.android.speeddial.SpeedDialView
import com.zywczas.letsshare.R

fun SpeedDialView.dimBackgroundOnMainButtonClick(activity: Activity, mainLayout: View){
    val window = activity.window

    setOnChangeListener(object : SpeedDialView.OnChangeListener{
        override fun onMainActionSelected(): Boolean = false

        override fun onToggleChanged(isOpen: Boolean) {
            if (isOpen){
                window.statusBarColor = ContextCompat.getColor(mainLayout.context, R.color.primaryVariantAlpha03)
                mainLayout.alpha = 0.3F
            } else {
                window.statusBarColor = ContextCompat.getColor(mainLayout.context, R.color.primaryVariant)
                mainLayout.alpha = 1F
            }
        }
    })
}