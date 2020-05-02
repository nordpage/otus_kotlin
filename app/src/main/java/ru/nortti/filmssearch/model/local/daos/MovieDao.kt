package ru.nortti.filmssearch.model.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.nortti.filmssearch.model.remote.MovieResponse

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: MovieResponse)

    @Delete
    fun deleteMovie(movie: MovieResponse)

    @Query("SELECT * FROM movie_table")
    fun getAllMoviesLiveData() : LiveData<MovieResponse>

}