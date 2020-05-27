package ru.nortti.filmssearch.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(val context: Context) {
    private val prefsName = "FIND_FILMS"
    private val sharedPref: SharedPreferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
    fun save(KEY_NAME: String, text: String) {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putString(KEY_NAME, text)

        editor.apply()
    }
    private fun save(value: Long) {
        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putLong(UPDATE_TIME, value)

        editor.apply()
    }

    fun setUpdateTime(time: Long) {
        save(time)
    }

    fun getUpdateTime() : Long {
        return sharedPref.getLong(UPDATE_TIME, 0L)
    }

    fun getValueString(KEY_NAME: String): String? {

        return sharedPref.getString(KEY_NAME, null)


    }

}
