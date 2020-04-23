package ru.nortti.filmssearch.arch.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.nortti.filmssearch.App
import ru.nortti.filmssearch.Constants
import ru.nortti.filmssearch.SharedPreference
import ru.nortti.filmssearch.network.ApiInteractor
import ru.nortti.filmssearch.network.models.MovieDetail
import ru.nortti.filmssearch.network.models.MovieResponce

class DetailsViewModel : ViewModel() {
    private val moviesLiveData = MutableLiveData<MovieDetail>()
    private val errorLiveData = MutableLiveData<String>()
    private val apiInteractor = App.getInstance().getInteractor()
    private val prefs: SharedPreference = SharedPreference(App.getInstance().applicationContext)
    val movies : LiveData<MovieDetail>
        get() = moviesLiveData

    val errors: LiveData<String>
        get() = errorLiveData


    fun getDetailedData(id: Int) {
        apiInteractor.getDetailedMovie(Constants.API_KEY, prefs.getValueString(Constants.LANGUAGE)!!, id, object : ApiInteractor.GetMoviesDetails {
            override fun onSuccess(movies: MovieDetail) {
                moviesLiveData.postValue(movies)
            }

            override fun onError(error: String) {
                errorLiveData.postValue(error)
            }

        })
    }
}
