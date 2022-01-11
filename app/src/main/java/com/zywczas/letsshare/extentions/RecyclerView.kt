package com.zywczas.letsshare.extentions

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.zywczas.letsshare.R

fun RecyclerView.addTransparentItemDivider(){ //todo poprawic nazwe bo to jest tylko dla horizontal
    val itemDivider = DividerItemDecoration(context, RecyclerView.HORIZONTAL)
    ContextCompat.getDrawable(context, R.drawable.item_divider_transparent)?.let {
        itemDivider.setDrawable(it)
    }
    addItemDecoration(itemDivider)
}