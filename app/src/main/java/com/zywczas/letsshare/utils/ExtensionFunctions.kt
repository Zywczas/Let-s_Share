package com.zywczas.letsshare.utils

import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun logD(msg : String) = Log.d("LetsShareTag", msg)
fun logD(e : Exception) = Log.d("LetsShareTag", e.toString())

//todo pozniej to usunac i zamienic wszedzie na snackbar albo alert dialog
fun Fragment.showToast(@StringRes msg : Int) = Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()