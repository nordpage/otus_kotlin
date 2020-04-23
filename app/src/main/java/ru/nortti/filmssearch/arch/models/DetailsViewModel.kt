package ru.nortti.filmssearch.arch.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.nortti.filmssearch.App
import ru.nortti.filmssearch.Constants
import ru.nortti.filmssearch.SharedPreference
import ru.nortti.filmssearch.network.ApiInteractor
import ru.nortti.filmssearch.network.models.ErrorResponse
import ru.nortti.filmssearch.network.models.MovieDetail

class DetailsViewModel : ViewModel() {
    private val moviesLiveData = MutableLiveData<MovieDetail>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val customErrorLiveData = MutableLiveData<ErrorResponse>()

    private val apiInteractor = App.getInstance().getInteractor()
    private val prefs: SharedPreference = SharedPreference(App.getInstance().applicationContext)
    val movies : LiveData<MovieDetail>
        get() = moviesLiveData

    val errors: LiveData<Throwable>
        get() = errorLiveData

    val customErrors: LiveData<ErrorResponse>
        get() = customErrorLiveData


    fun getDetailedData(id: Int) {
        apiInteractor.getDetailedMovie(Constants.API_KEY, prefs.getValueString(Constants.LANGUAGE)!!, id, object : ApiInteractor.GetMoviesDetails {
            override fun onSuccess(movies: MovieDetail) {
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
