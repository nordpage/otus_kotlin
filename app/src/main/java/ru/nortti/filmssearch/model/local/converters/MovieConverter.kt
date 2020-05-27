package ru.nortti.filmssearch.model.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.nortti.filmssearch.model.remote.Movie
import java.util.*
import java.util.stream.Collectors


class MovieConverter {

        private val gson = Gson()
        @TypeConverter
        fun stringToList(data: String?): List<Movie> {
                if (data == null) {
                        return Collections.emptyList()
                }

                val listType = object : TypeToken<List<Movie>>() {

                }.type

                return gson.fromJson<List<Movie>>(data, listType)
        }

        @TypeConverter
        fun listToString(someObjects: List<Movie>): String {
                return gson.toJson(someObjects)
        }

}