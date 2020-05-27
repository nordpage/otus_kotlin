package ru.nortti.filmssearch.model.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity(tableName = "favorites")
class Favorite (
    @Expose
    @PrimaryKey
    val uid: Int? = null,
    val id: Int,
    val title : String,
    val poster: String)