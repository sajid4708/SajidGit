package com.sajid.zohogitapp.common.utils

import android.graphics.Bitmap

import com.google.gson.Gson




object PojoJsonutils {
    // Serialize a single object.
    fun serializeToJson(bmp: Bitmap?): String? {
        val gson = Gson()
        return gson.toJson(bmp)
    }

    // Deserialize to single object.
    fun deserializeFromJson(jsonString: String?): Bitmap? {
        val gson = Gson()
        return gson.fromJson(jsonString, Bitmap::class.java)
    }
}