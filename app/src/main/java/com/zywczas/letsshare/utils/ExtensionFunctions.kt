package com.zywczas.letsshare.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.zywczas.letsshare.R
import java.text.SimpleDateFormat
import java.util.*

fun Any.logD(e: Exception) = logD(e.message)

fun Any.logD(msg: String?) = Log.d("LetsShareTag in ${this.javaClass.simpleName}", "$msg")

fun Fragment.showSnackbar(@StringRes msg: Int) = showSnackbar(getString(msg))

fun Fragment.showSnackbar(msg: String) {
    val snackbar = Snackbar.make(requireView(), msg, Snackbar.LENGTH_LONG)
        .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.onPrimary))
    val view = snackbar.view
    val textView = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
    textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryVariant))
    snackbar.show()
}

//fun Fragment.showToast(@StringRes msg: Int) = showToast(getString(msg))
//
//fun Fragment.showToast(msg: String) = Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()

fun Fragment.hideSoftKeyboard(){
    val inputManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (inputManager.isAcceptingText) {
        inputManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }
}

fun Fragment.turnOffOnBackPressed() = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){}

fun dateInPoland(): Date = Calendar.getInstance(LOCALE_POLAND).time

fun Date.monthId(): String = SimpleDateFormat("yyyy-MM", LOCALE_POLAND).format(this)

fun Date.dayFormat(): String = SimpleDateFormat("d.MM", LOCALE_POLAND).format(this)