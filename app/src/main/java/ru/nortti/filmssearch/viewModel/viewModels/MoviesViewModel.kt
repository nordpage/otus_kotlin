package ru.nortti.filmssearch.viewModel.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.nortti.filmssearch.model.remote.ErrorResponse
import ru.nortti.filmssearch.model.remote.MovieDetail
import ru.nortti.filmssearch.model.remote.MovieResponse
import ru.nortti.filmssearch.viewModel.repositories.MovieRepository

class MoviesViewModel(application: Application): AndroidViewModel(application) {
    private val moviesRepository = MovieRepository.getInstance(application)

    val movies : LiveData<MovieResponse>
        get() =  moviesRepository.getAllMoviesLiveData()

    val errors: LiveData<Throwable>
        get() = moviesRepository.getErrorData()

    val customErrors: LiveData<ErrorResponse>
        get() = moviesRepository.getCustomErrorData()

    fun onGetData() {
        moviesRepository.getAllMoviesLiveData()
    }

    fun onGetPagedData(page: Int) {
        moviesRepository.getMoviesPaginationLiveData(page)
    }
}