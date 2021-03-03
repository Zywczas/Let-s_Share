package com.zywczas.letsshare.utils

import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Any.logD(msg : String) = Log.d("LetsShareTag in ${this.javaClass.simpleName}", msg)
fun Any.logD(e : Exception) = Log.d("LetsShareTag  in ${this.javaClass.simpleName}", "${e.message}")

//todo pozniej to usunac i zamienic wszedzie na snackbar albo alert dialog
fun Fragment.showToast(@StringRes msg : Int) = Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
fun Fragment.showToast(msg : String) = Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()