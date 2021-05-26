package com.zywczas.letsshare.extentions

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.showSnackbar(@StringRes msg: Int) = showSnackbar(getString(msg))

fun Fragment.showSnackbar(msg: String) = Snackbar.make(requireView(), msg, Snackbar.LENGTH_LONG).show()

fun Fragment.hideSoftKeyboard(){
    val inputManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (inputManager.isAcceptingText) {
        inputManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }
}