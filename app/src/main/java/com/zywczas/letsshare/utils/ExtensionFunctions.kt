package com.zywczas.letsshare.utils

import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

fun Any.logD(msg : String) = Log.d("LetsShareTag in ${this.javaClass.simpleName}", msg)
fun Any.logD(e : Exception) = Log.d("LetsShareTag  in ${this.javaClass.simpleName}", "${e.message}")

//todo pozniej to usunac i zamienic wszedzie na snackbar albo alert dialog
fun Fragment.showToast(@StringRes msg : Int) = Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()

fun Fragment.showToast(msg : String) = Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()

fun Fragment.hideSoftKeyboard(){
    val inputManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (inputManager.isAcceptingText) {
        inputManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }
}

fun Date.getMonthId(): String = SimpleDateFormat("MM.yyyy", Locale.GERMANY).format(this)

fun Date.getCurrentDateString(): String = SimpleDateFormat("d.MM.yyyy", Locale.GERMANY).format(this)
