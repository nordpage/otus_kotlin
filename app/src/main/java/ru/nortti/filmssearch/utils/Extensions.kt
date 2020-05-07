package ru.nortti.filmssearch.utils

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.work.Data
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import ru.nortti.filmssearch.model.local.models.Favorite
import ru.nortti.filmssearch.model.remote.Movie
import java.time.format.DateTimeFormatter
import java.util.*

object Extensions {
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

    fun getImageUrlBig(poster: String): String {
        val url = StringBuilder()
        url.append("https://image.tmdb.org/t/p/w342").append(poster)
        return url.toString()
    }

    public fun toFavorite(item: Movie) : Favorite {
       return Favorite(id = item.id, title = item.title, poster = item.poster_path)
    }

    fun createWorkInputData(title: String, text: String, id: Int) : Data {
        return Data.Builder()
            .putString(EXTRA_TITLE, title)
            .putString(EXTRA_TEXT, text)
            .putInt(EXTRA_ID, id)
            .build()
    }
    fun getAlertTime(time: String) : Long {
        var formatter = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm")
        var date = DateTime.parse(time,formatter).withZone(DateTimeZone.forID("Europe/Moscow"))
        return date.millis
    }

    fun generateKey() : String {
        return UUID.randomUUID().toString()
    }

}