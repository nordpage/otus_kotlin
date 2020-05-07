package ru.nortti.filmssearch.utils

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.nortti.filmssearch.model.remote.Movie

class SharedPreference(val context: Context) {
    private val prefsName = "FIND_FILMS"
    private val sharedPref: SharedPreferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
    private var films: MutableList<Movie> = mutableListOf()
    fun save(KEY_NAME: String, text: String) {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putString(KEY_NAME, text)

        editor.apply()
    }

    fun addToFav(film: Movie) {
        films.add(film)
        save(films)
    }

    fun addToFav(pos: Int, film: Movie) {
        films.add(pos, film)
        save(films)
    }

    fun removeFromFav(film: Movie){
        if (films.contains(film)) {
            films.remove(film)
            save(films)
        }
    }
    private fun save(tasks: List<Movie>) {
        val gson = Gson()
        val editor: SharedPreferences.Editor = sharedPref.edit()
        val json = gson.toJson(tasks)
        editor.putString(TASKS, json)
        editor.apply()
    }

    fun getValueString(KEY_NAME: String): String? {

        return sharedPref.getString(KEY_NAME, null)


    }

    fun getFavorites() : MutableList<Movie> {
        val gson = Gson()
        val json = sharedPref.getString(TASKS,"")
        if (!TextUtils.isEmpty(json)) {
            films.clear()
            val listType = object : TypeToken<List<Movie>>() {}.type
            films.addAll(gson.fromJson(json, listType))
        }
        return films
    }

}
