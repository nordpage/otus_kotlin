package ru.nortti.filmssearch.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.nortti.filmssearch.model.local.daos.MovieDao
import ru.nortti.filmssearch.model.remote.Movie
import ru.nortti.filmssearch.utils.ROOM_DB_NAME

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class RoomDB : RoomDatabase() {

    abstract fun getMoviesDao() : MovieDao

    companion object {
        private var INSTANCE : RoomDB?  = null

        fun getDatabase(context: Context) = INSTANCE ?: kotlin.run {
            Room.databaseBuilder(context.applicationContext, RoomDB::class.java,
               ROOM_DB_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}