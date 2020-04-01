package ru.nortti.filmssearch

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.nortti.filmssearch.Constants.TASKS

class SharedPreference(val context: Context) {
    private val PREFS_NAME = "FIND_FILMS"
    val sharedPref: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    var films: MutableList<Film> = mutableListOf()
    fun save(KEY_NAME: String, text: String) {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putString(KEY_NAME, text)

        editor!!.commit()
    }

    fun save(KEY_NAME: String, value: Int) {
        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putInt(KEY_NAME, value)

        editor.commit()
    }

    fun save(KEY_NAME: String, status: Boolean) {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putBoolean(KEY_NAME, status!!)

        editor.apply()
    }
    fun addToFav(film: Film) {
        films.add(film)
        save(films)
    }

    fun removeFromFav(film: Film){
        if (films.contains(film)) {
            films.remove(film)
            save(films)
        }
    }
    fun save(tasks: List<Film>) {
        val gson = Gson()
        val editor: SharedPreferences.Editor = sharedPref.edit()
        val json = gson.toJson(tasks)
        editor.putString(TASKS, json)
        editor.apply()
    }

    fun getValueString(KEY_NAME: String): String? {

        return sharedPref.getString(KEY_NAME, null)


    }

    fun getFavorites() : MutableList<Film> {
        val gson = Gson()
        val json = sharedPref.getString(TASKS,"")
        if (!TextUtils.isEmpty(json)) {
            films.clear()
            val listType = object : TypeToken<List<Film>>() {}.type
            films.addAll(gson.fromJson(json, listType))
        }
        return films
    }

    fun getValueInt(KEY_NAME: String): Int {

        return sharedPref.getInt(KEY_NAME, 0)
    }

    fun getValueBoolean(KEY_NAME: String, defaultValue: Boolean): Boolean {

        return sharedPref.getBoolean(KEY_NAME, defaultValue)

    }

    fun clearSharedPreference() {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        //sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        editor.clear()
        editor.commit()
    }

    fun removeValue(KEY_NAME: String) {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.remove(KEY_NAME)
        editor.commit()
    }
}
