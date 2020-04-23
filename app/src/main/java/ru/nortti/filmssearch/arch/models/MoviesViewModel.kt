package ru.nortti.filmssearch.arch.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.nortti.filmssearch.App
import ru.nortti.filmssearch.Constants
import ru.nortti.filmssearch.SharedPreference
import ru.nortti.filmssearch.network.ApiInteractor
import ru.nortti.filmssearch.network.models.MovieResponce

class MoviesViewModel: ViewModel() {
    private val moviesLiveData = MutableLiveData<MovieResponce>()
    private val errorLiveData = MutableLiveData<String>()
    private val apiInteractor = App.getInstance().getInteractor()
    private val prefs: SharedPreference = SharedPreference(App.getInstance().applicationContext)
    val movies : LiveData<MovieResponce>
        get() = moviesLiveData

    val errors: LiveData<String>
        get() = errorLiveData

    fun onGetData() {
        apiInteractor.getTopMovies(Constants.API_KEY, prefs.getValueString(Constants.LANGUAGE)!!, object : ApiInteractor.GetMoviesCallback {
            override fun onSuccess(movies: MovieResponce) {
                moviesLiveData.postValue(movies)
            }

            override fun onError(error: String) {
                errorLiveData.postValue(error)
            }

        })
    }

    fun onGetPagedData(page: Int) {
        apiInteractor.getTopMoviesPagination(Constants.API_KEY, prefs.getValueString(Constants.LANGUAGE)!!, page, object : ApiInteractor.GetMoviesCallback {
            override fun onSuccess(movies: MovieResponce) {
                moviesLiveData.postValue(movies)
            }

            override fun onError(error: String) {
                errorLiveData.postValue(error)
            }

        })
    }
}