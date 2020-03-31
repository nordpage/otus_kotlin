package ru.nortti.filmssearch

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import java.util.*

object Constants {
    var LANGUAGE = "language"
    var THEME = "theme"

    fun setLocale(context: Context, langCode: String){
        var config = Configuration(context.resources.configuration)
        var locale = Locale(langCode.toLowerCase())
        Locale.setDefault(locale)
        config.setLocale(locale)
        context.resources.updateConfiguration(config,context.resources.displayMetrics)
    }

    fun setAppTheme(theme: String) {
        when(theme) {
            "DAY" ->  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "NIGHT" ->  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}