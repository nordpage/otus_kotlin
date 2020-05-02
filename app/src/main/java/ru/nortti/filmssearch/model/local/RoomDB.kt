package ru.nortti.filmssearch.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.nortti.filmssearch.model.local.converters.MovieConverter
import ru.nortti.filmssearch.model.local.daos.FavoritesDao
import ru.nortti.filmssearch.model.local.daos.MovieDao
import ru.nortti.filmssearch.model.local.models.Favorite
import ru.nortti.filmssearch.model.remote.MovieResponse
import ru.nortti.filmssearch.utils.ROOM_DB_NAME

@Database(entities = [MovieResponse::class, Favorite::class], version = 1, exportSchema = false)
@TypeConverters(MovieConverter::class)
abstract class RoomDB : RoomDatabase() {

    abstract fun getMoviesDao() : MovieDao
    abstract fun getFavoritesDao() : FavoritesDao

    companion object {
        private var INSTANCE : RoomDB?  = null

        fun getDatabase(context: Context) = INSTANCE ?: kotlin.run {
            Room.databaseBuilder(context.applicationContext, RoomDB::class.java,
               ROOM_DB_NAME
            )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}