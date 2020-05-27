package ru.nortti.filmssearch.viewModel.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.nortti.filmssearch.App
import ru.nortti.filmssearch.api.ApiInteractor
import ru.nortti.filmssearch.model.local.RoomDB
import ru.nortti.filmssearch.model.local.daos.MovieDao
import ru.nortti.filmssearch.model.remote.ErrorResponse
import ru.nortti.filmssearch.model.remote.Movie
import ru.nortti.filmssearch.model.remote.MovieResponse
import ru.nortti.filmssearch.utils.API_KEY
import ru.nortti.filmssearch.utils.LANGUAGE
import ru.nortti.filmssearch.utils.LIMIT_TIME
import ru.nortti.filmssearch.utils.SharedPreference

class MovieRepository private constructor(application: Application){

    private val movieDAO : MovieDao = RoomDB.getDatabase(application).getMoviesDao()
    private val prefs: SharedPreference = SharedPreference(App.getInstance().applicationContext)
    private val apiInteractor = App.getInstance().getInteractor()
    private val moviesLiveData = MutableLiveData<MovieResponse>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val customErrorLiveData = MutableLiveData<ErrorResponse>()

    private fun insertMovie(response: MovieResponse) {
        movieDAO.insertMovie(response)
    }

    private fun deleteMovie(response: MovieResponse) {
        movieDAO.deleteMovie(response)
    }

    fun getAllMoviesLiveData() : LiveData<MovieResponse> {
        if (System.currentTimeMillis() - prefs.getUpdateTime() > LIMIT_TIME) {
            getAllMoviesFromRemote()
            return moviesLiveData
        } else {
            return movieDAO.getAllMoviesLiveData()
        }
    }

    fun getAllMoviesFromRemote() {
        apiInteractor.getTopMovies(
            API_KEY, prefs.getValueString(
                LANGUAGE
            )!!, object : ApiInteractor.GetMoviesCallback {
                override fun onSuccess(movies: MovieResponse) {
                    insertMovie(movies)
                    prefs.setUpdateTime(System.currentTimeMillis())
                    moviesLiveData.postValue(movies)
                }

                override fun onError(error: Throwable) {
                    errorLiveData.postValue(error)
                }

                override fun onCustomError(error: ErrorResponse) {
                    customErrorLiveData.postValue(error)
                }

            })
    }

    fun getMoviesPaginationLiveData(page: Int) : LiveData<MovieResponse> {
        return movieDAO.getAllMoviesLiveData()
    }

    private fun getMoviesPaginationFromRemote(page: Int) {
        apiInteractor.getTopMoviesPagination(
            API_KEY, prefs.getValueString(
                LANGUAGE)!!, page, object : ApiInteractor.GetMoviesCallback {
                override fun onSuccess(movies: MovieResponse) {
                    insertMovie(movies)
                }

                override fun onError(error: Throwable) {
                    errorLiveData.postValue(error)
                }

                override fun onCustomError(error: ErrorResponse) {
                    customErrorLiveData.postValue(error)
                }

            })
    }

    fun getMoviesData() : LiveData<MovieResponse> {
        return moviesLiveData
    }

    fun getErrorData() : LiveData<Throwable> {
        return errorLiveData
    }

    fun getCustomErrorData() : LiveData<ErrorResponse> {
        return customErrorLiveData
    }

    companion object {
        private var INSTANCE : MovieRepository? = null

        fun getInstance(application: Application): MovieRepository = INSTANCE ?: kotlin.run {
            INSTANCE = MovieRepository(application)
            INSTANCE!!
        }
    }

}