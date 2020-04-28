package ru.nortti.filmssearch.viewModel.repositories

import android.app.Application
import ru.nortti.filmssearch.model.local.RoomDB
import ru.nortti.filmssearch.model.local.daos.MovieDao

class MovieRepository private constructor(application: Application){

    private val movieDAO : MovieDao = RoomDB.getDatabase(application).getMoviesDao()


}