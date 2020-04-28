package ru.nortti.filmssearch.model.remote

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import ru.nortti.filmssearch.model.remote.Movie

data class MovieResponse(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)