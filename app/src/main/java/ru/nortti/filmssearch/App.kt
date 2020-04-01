package ru.nortti.filmssearch

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import ru.nortti.filmssearch.Constants.LANGUAGE
import ru.nortti.filmssearch.Constants.THEME

class App : Application() {

    companion object {
        lateinit var prefs: SharedPreference
    }
    override fun onCreate() {
        prefs = SharedPreference(applicationContext)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        if  (prefs.getValueString(LANGUAGE) == null) prefs.save(LANGUAGE, "RU")
        if  (prefs.getValueString(THEME) == null) prefs.save(THEME, "DAY")
        super.onCreate()
    }
}