package ru.nortti.filmssearch.viewModel.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TransferViewModel: ViewModel() {

    private val movieIdLiveData = MutableLiveData<Int>()

    fun setMovieIdData(id: Int) {
        movieIdLiveData.postValue(id)
    }

    fun getMovieIdData() : Int {
        return movieIdLiveData.value!!
    }

}