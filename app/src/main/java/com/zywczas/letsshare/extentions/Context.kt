package com.zywczas.letsshare.extentions

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.google.android.material.color.MaterialColors

//@ColorInt
//fun Context.getColorFromAttr(@AttrRes attrColor: Int, typedValue: TypedValue = TypedValue(), resolveRefs: Boolean = true): Int {
//    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
//    return typedValue.data
//}

@ColorInt
fun Context.getColorFromAttr(@AttrRes attrColor: Int): Int = MaterialColors.getColor(this, attrColor, Color.BLACK)
