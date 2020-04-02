package ru.nortti.filmssearch

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import java.util.*

object Constants {
    var LANGUAGE = "language"
    var THEME = "theme"
    var TASKS = "tasks"

    var BASE_URL = "https://api.themoviedb.org/3/"
    var API_KEY = "a1f3b8db759ad54efeb48ffa8aaf60e3"

    var PAGE_START = 1
    var TOTAL_PAGES = 5

    fun setLocale(context: Context, langCode: String) {
        var config = Configuration(context.resources.configuration)
        var locale = Locale(langCode.toLowerCase())
        Locale.setDefault(locale)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    fun setAppTheme(theme: String) {
        when (theme) {
            "DAY" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "NIGHT" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    fun getImageUrl(poster: String): String {
        val url = StringBuilder()
        url.append("https://image.tmdb.org/t/p/w154").append(poster)
        return url.toString()
    }


}