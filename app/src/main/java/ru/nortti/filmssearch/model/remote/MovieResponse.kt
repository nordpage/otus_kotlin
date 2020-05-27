package ru.nortti.filmssearch.model.remote

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity(tableName = "movie_table")
data class MovieResponse(
    @Expose
    @PrimaryKey
    val uid: Int,
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)