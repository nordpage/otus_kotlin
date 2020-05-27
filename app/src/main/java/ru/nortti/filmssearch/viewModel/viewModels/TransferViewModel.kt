package ru.nortti.filmssearch.viewModel.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TransferViewModel: ViewModel() {

    private val movieIdLiveData = MutableLiveData<Int>()

    val movieId : LiveData<Int>
        get() = movieIdLiveData

    fun setMovieIdData(id: Int) {
        movieIdLiveData.postValue(id)
    }

    fun getMovieIdData() : Int {
        return movieIdLiveData.value!!
    }

}