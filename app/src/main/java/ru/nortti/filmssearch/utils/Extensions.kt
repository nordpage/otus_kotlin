package ru.nortti.filmssearch.utils

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import ru.nortti.filmssearch.model.local.models.Favorite
import ru.nortti.filmssearch.model.remote.Movie
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
}