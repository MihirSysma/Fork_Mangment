package com.forkmang.helper

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

/**
 * Created by Mobile on 26-09-2017.
 */
class StorePreference(context: Context) {
    private val preferences: SharedPreferences
    private var editor: SharedPreferences.Editor? = null

    init {
        preferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
    }

    fun setString(key: String?, value: String?) {
        editor = preferences.edit()
        editor?.putString(key, value)
        editor?.commit()
    }

    fun getString(key: String?): String? {
        return preferences.getString(key, "")
    }

    fun setInt(key: String?, value: Int?) {
        editor = preferences.edit()
        editor?.putInt(key, value!!)
        editor?.commit()
    }

    fun setImage(key: String?, yourbitmap: Bitmap) {
        editor = preferences.edit()
        editor?.putString(key, encodeTobase64(yourbitmap))
        editor?.commit()
    }

    fun getImage(key: String?): Bitmap {
        val input = preferences.getString(key, "")
        val decodedByte = Base64.decode(input, 0)
        return BitmapFactory
            .decodeByteArray(decodedByte, 0, decodedByte.size)
    }

    fun clear() {
        val editor = preferences.edit()
        editor.clear()
        editor.commit()
    }

    fun getInt(key: String?): Int {
        return preferences.getInt(key, 0)
    }

    fun getBoolean(key: String?): Boolean {
        return preferences.getBoolean(key, false)
    }

    fun setBoolean(key: String?, value: Boolean) {
        editor = preferences.edit()
        editor?.putBoolean(key, value)
        editor?.commit()
    }

    fun getData(id: String?): String? {
        return preferences.getString(id, "")
    }

    fun setData(id: String?, `val`: String?) {
        editor = preferences.edit()
        editor?.putString(id, `val`)
        editor?.commit()
    }

    fun getCoordinates(id: String?): String? {
        return preferences.getString(id, "0")
    }

    companion object {
        fun encodeTobase64(image: Bitmap): String {
            val baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val b = baos.toByteArray()

            //Log.d("Image Log:", imageEncoded);
            return Base64.encodeToString(b, Base64.DEFAULT)
        }
    }
}