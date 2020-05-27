package ru.nortti.filmssearch.viewModel.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.nortti.filmssearch.App
import ru.nortti.filmssearch.api.ApiInteractor
import ru.nortti.filmssearch.model.remote.ErrorResponse
import ru.nortti.filmssearch.model.remote.MovieDetail
import ru.nortti.filmssearch.model.remote.MovieResponse
import ru.nortti.filmssearch.utils.API_KEY
import ru.nortti.filmssearch.utils.LANGUAGE
import ru.nortti.filmssearch.utils.SharedPreference

class DetailsRepository{

    private val prefs: SharedPreference = SharedPreference(App.getInstance().applicationContext)
    private val apiInteractor = App.getInstance().getInteractor()
    private val detailsLiveData = MutableLiveData<MovieDetail>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val customErrorLiveData = MutableLiveData<ErrorResponse>()

    fun getMovieDetails(id: Int) {
        apiInteractor.getDetailedMovie(API_KEY,
            prefs.getValueString(LANGUAGE)!!,
            id,
            object : ApiInteractor.GetMoviesDetails {
                override fun onSuccess(movies: MovieDetail) {
                    detailsLiveData.postValue(movies)
                }

                override fun onError(error: Throwable) {
                    errorLiveData.postValue(error)
                }

                override fun onCustomError(error: ErrorResponse) {
                    customErrorLiveData.postValue(error)
                }

            })
    }

    fun getDetailedData() : LiveData<MovieDetail> {
        return detailsLiveData
    }

    fun getErrorData() : LiveData<Throwable> {
        return errorLiveData
    }

    fun getCustomErrorData() : LiveData<ErrorResponse> {
        return customErrorLiveData
    }


    companion object {
        private var INSTANCE : DetailsRepository? = null

        fun getInstance(): DetailsRepository = INSTANCE ?: kotlin.run {
            INSTANCE = DetailsRepository()
            INSTANCE!!
        }
    }
}