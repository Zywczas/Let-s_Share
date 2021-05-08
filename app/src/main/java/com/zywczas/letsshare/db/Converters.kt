package com.zywczas.letsshare.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun toString(value: List<String>): String {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().toJson(value, type)
    }

    @TypeConverter
    fun fromString(value: String): List<String>{
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, type)
    }

}