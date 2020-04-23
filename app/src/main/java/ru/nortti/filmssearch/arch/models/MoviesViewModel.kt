package ru.nortti.filmssearch.arch.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.nortti.filmssearch.App
import ru.nortti.filmssearch.Constants
import ru.nortti.filmssearch.SharedPreference
import ru.nortti.filmssearch.network.ApiInteractor
import ru.nortti.filmssearch.network.models.ErrorResponse
import ru.nortti.filmssearch.network.models.MovieResponse

class MoviesViewModel: ViewModel() {
    private val moviesLiveData = MutableLiveData<MovieResponse>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val customErrorLiveData = MutableLiveData<ErrorResponse>()
    private val apiInteractor = App.getInstance().getInteractor()
    private val prefs: SharedPreference = SharedPreference(App.getInstance().applicationContext)
    val movies : LiveData<MovieResponse>
        get() = moviesLiveData

    val errors: LiveData<Throwable>
        get() = errorLiveData

    val customErrors: LiveData<ErrorResponse>
        get() = customErrorLiveData

    fun onGetData() {
        apiInteractor.getTopMovies(Constants.API_KEY, prefs.getValueString(Constants.LANGUAGE)!!, object : ApiInteractor.GetMoviesCallback {
            override fun onSuccess(movies: MovieResponse) {
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

    fun onGetPagedData(page: Int) {
        apiInteractor.getTopMoviesPagination(Constants.API_KEY, prefs.getValueString(Constants.LANGUAGE)!!, page, object : ApiInteractor.GetMoviesCallback {
            override fun onSuccess(movies: MovieResponse) {
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
}