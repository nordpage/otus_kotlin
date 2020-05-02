package ru.nortti.filmssearch.viewModel.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.nortti.filmssearch.App
import ru.nortti.filmssearch.utils.SharedPreference
import ru.nortti.filmssearch.api.ApiInteractor
import ru.nortti.filmssearch.model.remote.ErrorResponse
import ru.nortti.filmssearch.model.remote.MovieDetail
import ru.nortti.filmssearch.utils.API_KEY
import ru.nortti.filmssearch.utils.LANGUAGE
import ru.nortti.filmssearch.viewModel.repositories.DetailsRepository

class DetailsViewModel : ViewModel() {

    private val detailsRepository = DetailsRepository.getInstance()


    val movies : LiveData<MovieDetail>
        get() = detailsRepository.getDetailedData()

    val errors: LiveData<Throwable>
        get() = detailsRepository.getErrorData()

    val customErrors: LiveData<ErrorResponse>
        get() = detailsRepository.getCustomErrorData()


    fun getDetailedData(id: Int) {
        detailsRepository.getMovieDetails(id)
    }
}
