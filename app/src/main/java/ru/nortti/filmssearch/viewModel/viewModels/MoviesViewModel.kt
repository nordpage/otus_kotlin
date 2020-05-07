package ru.nortti.filmssearch.viewModel.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.nortti.filmssearch.App
import ru.nortti.filmssearch.utils.SharedPreference
import ru.nortti.filmssearch.api.ApiInteractor
import ru.nortti.filmssearch.model.remote.ErrorResponse
import ru.nortti.filmssearch.model.remote.MovieResponse
import ru.nortti.filmssearch.utils.API_KEY
import ru.nortti.filmssearch.utils.LANGUAGE

class MoviesViewModel: ViewModel() {
    private val moviesLiveData = MutableLiveData<MovieResponse>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val customErrorLiveData = MutableLiveData<ErrorResponse>()
    private val apiInteractor = App.getInstance().getInteractor()
    private val prefs: SharedPreference =
        SharedPreference(App.getInstance().applicationContext)
    val movies : LiveData<MovieResponse>
        get() = moviesLiveData

    val errors: LiveData<Throwable>
        get() = errorLiveData

    val customErrors: LiveData<ErrorResponse>
        get() = customErrorLiveData

    fun onGetData() {
        apiInteractor.getTopMovies(
            API_KEY, prefs.getValueString(
                LANGUAGE)!!, object : ApiInteractor.GetMoviesCallback {
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
        apiInteractor.getTopMoviesPagination(
            API_KEY, prefs.getValueString(
                LANGUAGE)!!, page, object : ApiInteractor.GetMoviesCallback {
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