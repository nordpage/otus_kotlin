package ru.nortti.filmssearch.model.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity(tableName = "pending")
class Pending(
    @Expose
    @PrimaryKey
    var uid: Int? = null,
    var id: Int,
    var title: String,
    var poster: String,
    var time: Long
)