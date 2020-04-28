package ru.nortti.filmssearch.model.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.nortti.filmssearch.model.remote.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)

    @Delete
    fun deleteMovie(movie: Movie)

    @Query("SELECT * FROM movie_table")
    fun getAllMoviesLiveData() : LiveData<List<Movie>>

}