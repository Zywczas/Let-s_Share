package com.zywczas.letsshare.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.leinardi.android.speeddial.SpeedDialView
import com.zywczas.letsshare.R
import java.text.SimpleDateFormat
import java.util.*

fun Any.logD(e: Exception) = logD(e.message)

fun Any.logD(msg: String?) = Log.d("LetsShareTag in ${this.javaClass.simpleName}", "$msg")

fun Fragment.showSnackbar(@StringRes msg: Int) = showSnackbar(getString(msg))

fun Fragment.showSnackbar(msg: String) = Snackbar.make(requireView(), msg, Snackbar.LENGTH_LONG).show()

fun Fragment.hideSoftKeyboard(){
    val inputManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (inputManager.isAcceptingText) {
        inputManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }
}

fun dateInPoland(): Date = Calendar.getInstance(LOCALE_POLAND).time

fun Date.monthId(): String = SimpleDateFormat("yyyy-MM", LOCALE_POLAND).format(this)

fun Date.dayFormat(): String = SimpleDateFormat("d.MM", LOCALE_POLAND).format(this)

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
